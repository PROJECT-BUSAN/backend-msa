import urllib
import os
import mimetypes

from django.db import transaction
from django.db.models.aggregates import Count
from django.db.models import Q, F
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from django.http.response import HttpResponse

from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.exceptions import NotFound
from rest_framework.generics import mixins, GenericAPIView
from rest_framework.response import Response
from rest_framework.views import APIView

from api.mixins import PublicApiMixin

from board.serializers import CategorySerializer, PostSerializer, FileSerializer
from board.models import Category, Post, PostFile


User = get_user_model()


class CategoryCreateReadApi(PublicApiMixin,
                            mixins.ListModelMixin,
                            mixins.CreateModelMixin,
                            viewsets.GenericViewSet):
    
    queryset = Category.objects.all()
    serializer_class = CategorySerializer
    
    def get_queryset(self):
        if self.action == 'list':
            return Category.objects.select_related('creator').all()
        return super().get_queryset()


class CategoryDetailApi(PublicApiMixin,
                        mixins.RetrieveModelMixin,
                        mixins.UpdateModelMixin,
                        mixins.DestroyModelMixin,
                        viewsets.GenericViewSet):
    
    queryset = Category.objects.all()
    serializer_class = CategorySerializer
    lookup_url_kwarg = 'cate_id'


class PostCreateReadApi(PublicApiMixin, 
                        mixins.ListModelMixin,
                        mixins.CreateModelMixin,
                        viewsets.GenericViewSet):
    
    queryset = Post.objects.all()
    serializer_class = PostSerializer
    lookup_url_kwarg = 'cate_id'
    ordering = ['-created_date']
    
    def get_queryset(self):
        if self.action == 'list':
            kw = self.request.GET.get("kw", '')
            cate_pk = self.kwargs[self.lookup_url_kwarg]
            condition = Q(category__pk=cate_pk)
            if kw:
                condition.add(
                    Q(title__icontains=kw) | 
                    Q(content__icontains=kw) |
                    Q(creator__profile__nickname__icontains=kw) 
                    ,Q.AND
                )
            
            queryset = Post.objects.select_related(
                'creator'
            ).annotate(
                nickname=F('creator__profile__nickname'),
                favorite_count=Count('favorite_user'),
            ).filter(
                condition
            )
            return queryset
        else:
            return super().get_queryset()
    
    @transaction.atomic()
    def create(self, request, *args, **kwargs):
        return super().create(request, *args, **kwargs)
    
    def perform_create(self, serializer):
        post = serializer.save()
        files = self.request.FILES.getlist('postfile')
        for file in files:
            postfile = PostFile(
                upload_files=file,
                filename=file.name,
                post=post
            )
            postfile.save()
            

class PostDetailApi(PublicApiMixin,
                    mixins.RetrieveModelMixin,
                    mixins.UpdateModelMixin,
                    mixins.DestroyModelMixin,
                    viewsets.GenericViewSet):
    
    queryset = Post.objects.all()
    serializer_class = PostSerializer
    lookup_url_kwarg = 'post_id'
    
    def get_queryset(self):
        if self.action == 'retrieve':
            pk = self.kwargs['post_id']
            print(pk)
            queryset = Post.objects.select_related(
                    'creator',
                    # 'creator__profile'
                ).prefetch_related(
                    'comment',
                    'comment__creator',
                    'comment__reply'
                ).filter(pk=pk)
            return queryset
        
        return super().get_queryset()
    
    def retrieve(self, request, *args, **kwargs):
        pk = kwargs['post_id']
        post = get_object_or_404(Post, pk=pk)
        expire_time = 600  #10분
        cookie_value = request.COOKIES.get('hitboard', '_')
        response = Response(status=status.HTTP_200_OK)
        
        if f'_{pk}_' not in cookie_value:
            cookie_value += f'{pk}_'
            response.set_cookie(
                'hitboard', value=cookie_value, 
                max_age=expire_time, httponly=True)

            post.hits += 1
            post.save()
            
        instance = self.get_object()
        serializer = self.get_serializer(instance)
        response.data = serializer.data
        return response
    
    
    @action(detail=True, methods="POST")
    def like(self, request, *args, **kwargs):
        pk = kwargs['post_id']
        user = request.user
        post = get_object_or_404(Post, pk=pk)
        
        if post.favorite_user.filter(pk=pk).exists():
            post.favorite_user.remove(user)
        else:
            post.favorite_user.add(user)
        return Response(status=status.HTTP_200_OK)


class PostFileDownloadApi(PublicApiMixin, APIView):
    def get(self, request, *args, **kwargs):
        file = get_object_or_404(PostFile, pk=kwargs['file_id'])
        url = file.upload_files.url[1:]
        file_url = urllib.parse.unquote(url)
        
        if os.path.exists(file_url):
            with open(file_url, 'rb') as f:
                quote_file_url = urllib.parse.quote(file.filename.encode('utf-8'))
                response = HttpResponse(
                    f.read(), content_type=mimetypes.guess_type(quote_file_url)[0]
                )
                response['Content-Disposition'] = 'attachment;filename*=UTF-8\'\'%s' % quote_file_url
                
                return response
        else:
            raise NotFound
        
from rest_framework import serializers, status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.generics import GenericAPIView, mixins

from django.db import transaction
from django.db.models import Q
from django.conf import settings
from django.core import exceptions
from django.contrib.auth import get_user_model
from django.contrib.auth.hashers import check_password
from django.core.management.utils import get_random_secret_key
from django.template.loader import render_to_string
from django.utils.translation import gettext_lazy as _

from api.mixins import ApiAuthMixin, PublicApiMixin

from auth.authenticate import jwt_login

from users.models import Profile
from users.serializers import RegisterSerializer, UserSerializer,\
    PasswordChangeSerializer, validate_password12
from users.services import send_mail, email_auth_string


User = get_user_model()


class UserMeApi(ApiAuthMixin, APIView):
    def get(self, request, *args, **kwargs):
        """
        현재 로그인 된 유저의 모든 정보 반환
        username, email, last_login, profile etc...
        """
        if request.user is None:
            raise exceptions.PermissionDenied('PermissionDenied')
        
        username = request.user.username
        
        user_query = User.objects\
            .filter(Q(username=username))\
            .prefetch_related(
                'profile__favorite_post',
                'profile__favorite_post__favorite_user',
                'profile__favorite_post__creator',
                'profile__favorite_post__category',
                'profile__favorite_category',
                'profile__favorite_category__favorite_user',
            )
        
        return Response(UserSerializer(user_query, many=True, context={'request':request}).data)
    
    def put(self, request, *args, **kwargs):
        """
        현재 로그인 된 유저의 비밀번호 변경
        """
        user = request.user
        if not check_password(request.data.get("oldpassword"), user.password):
            raise serializers.ValidationError(
                _("passwords do not match")
            )
        
        serializer = PasswordChangeSerializer(data=request.data, partial=True)
        
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_409_CONFLICT)
        
        
        validated_data = serializer.validated_data
        serializer.update(user=user, validated_data=validated_data)
        return Response({
            "message": "Change password success"
            }, status=status.HTTP_200_OK)
    
    
    def delete(self, request, *args, **kwargs):
        """
        현재 로그인 된 유저 삭제
        소셜 로그인 유저는 바로 삭제.
        일반 회원가입 유저는 비밀번호 입력 후 삭제.
        """
        user = request.user
        signup_path = user.profile.signup_path
        
        if signup_path == "kakao" or signup_path == "google":
            user.delete()
            return Response({
                "message": "Delete user success"
                }, status=status.HTTP_204_NO_CONTENT)
            
        if not check_password(request.data.get("password"), user.password):
            raise serializers.ValidationError(
                _("passwords do not match")
            )
            
        user.delete()
        
        return Response({
            "message": "Delete user success"
            }, status=status.HTTP_204_NO_CONTENT)


class UserCreateApi(PublicApiMixin, APIView):
    @transaction.atomic
    def post(self, request, *args, **kwargs):
        """
        회원가입 api
        user 모델과 profile 모델이 반드시 같이 생성되어야 하기 때문에
        transaction 적용
        """
        serializer = RegisterSerializer(data=request.data)
        if not serializer.is_valid(raise_exception=True):
            return Response({
                "message": "Request Body Error"
                }, status=status.HTTP_409_CONFLICT)
        
        user = serializer.save()
        
        response = Response(status=status.HTTP_200_OK)
        response = jwt_login(response=response, user=user)
        return response


class FindIDApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        유저 아이디 찾기
        email 입력을 통해 아이디를 찾을 수 있다.
        email 필드가 unique인 이유.
        """
        target_email = request.data.get('email', '')
        user = User.objects.filter(email=target_email)
        
        if not user.exists():
            return Response({
                "message": "user not found"
            }, status=status.HTTP_404_NOT_FOUND)
        
        data = {
            "username": user.first().username
        }
        
        return Response(data, status=status.HTTP_200_OK)


class SendPasswordEmailApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        비밀번호 변경 인증 코드 발송
        """
        target_username = request.data.get('username', '')
        target_email = request.data.get('email', '')
        
        target_user = User.objects.filter(
            username=target_username, 
            email=target_email
        )
        
        if target_user.exists():
            auth_string = email_auth_string()
            target_user.first().profile.auth = auth_string
            target_user.first().profile.save()
            
            try:
                send_mail(
                    'PROJECT 비밀번호 찾기 인증 메일입니다.',
                    recipient_list=[target_email],
                    html=render_to_string('recovery_email.html', {
                        'auth_string': auth_string,
                    })
                )
            except:
                return Response({
                    "message": "email error",
                }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
                
            return Response({
                "message": "Verification code sent"
            }, status=status.HTTP_200_OK)
            
        else:
            return Response({
                "message": "User does not exist"
            }, status=status.HTTP_404_NOT_FOUND)
            

class ConfirmPasswordEmailApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwagrs):
        """
        1. 인증 코드 확인
        2. 해당 username으로 로그인
        """
        target_username = request.data.get('username', '')
        target_code = request.data.get('code', '')
        user = User.objects.get(username=target_username)
        profile = user.profile
        
        if profile.auth == target_code:
            profile.auth = get_random_secret_key()
            profile.save()
            
            response = Response({
                "message": "Verification success",
                "user": target_username,
            }, status=status.HTTP_202_ACCEPTED)
            response = jwt_login(response=response, user=user)
            return response
        else:
            return Response({
                "message": "Verification Failed"
            }, status=status.HTTP_401_UNAUTHORIZED)


class ResetPasswordApi(ApiAuthMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        비밀번호 찾기 이메일 인증 이후
        비밀번호 변경 api
        """
        password1 = request.data.get('password1', '')
        password2 = request.data.get('password2', '')
        user = request.user
        
        newpassword = validate_password12(password1, password2)
        
        user.set_password(newpassword)
        user.save()
        
        response = Response({
            "message": "Reset password success! Go to login page"
        }, status=status.HTTP_202_ACCEPTED)
        response.delete_cookie(settings.JWT_AUTH['JWT_AUTH_COOKIE'])

        return response
    
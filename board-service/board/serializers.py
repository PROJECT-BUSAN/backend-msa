from rest_framework import serializers
from rest_framework.utils import model_meta
from rest_framework.exceptions import ValidationError
from rest_framework.fields import SerializerMethodField

from django.utils.translation import gettext_lazy as _
from django.utils import timezone
from django.db import transaction

from board.models import Category, Post, Comment, PostFile, Reply


class CategorySerializer(serializers.ModelSerializer):
    favorite_count = serializers.SerializerMethodField(read_only=True)
    created_date = serializers.DateTimeField(read_only=True)
    
    class Meta:
        model = Category
        fields = (
            'id', 
            'title', 
            'is_anonymous',
            'created_date',
            'top_fixed', 
            'only_superuser',
            'favorite_count',
            'creator',
        )
    
    def get_favorite_count(self, obj):
        return obj.favorite_user.count()
    
    def validate(self, attrs):
        if attrs['title'] is None:
            raise ValidationError(_("No title"))
        
        return attrs


class FileSerializer(serializers.ModelSerializer):
    class Meta:
        model = PostFile
        fields = (
            'id', 'filename',
        )
    

class PostSerializer(serializers.ModelSerializer):
    favorite_count = serializers.SerializerMethodField(read_only=True)
    hits = serializers.IntegerField(read_only=True)
    created_date = serializers.DateTimeField(read_only=True)
    modified_date = serializers.DateTimeField(read_only=True)
    postfile = FileSerializer(read_only=True, many=True)
    
    class Meta:
        model = Post
        fields = (
            'id',
            'title',
            'content',
            'thumbnail',
            'created_date',
            'hits',
            'modified_date',
            'top_fixed',
            'category',
            'creator',
            'favorite_count',
            'postfile',
        )
    
    def get_favorite_count(self, obj):
        return obj.favorite_user.count()
    
    def validate(self, attrs):
        if attrs['title'] is None:
            raise ValidationError(_("No title"))
        
        if attrs['content'] is None:
            raise ValidationError(_("No content"))
        
        return attrs
from uuid import uuid4
from datetime import datetime

from django.utils import timezone
from django.db import models
from django.conf import settings


User = settings.AUTH_USER_MODEL



def get_file_path(instance, filename):
    ymd_path = datetime.now().strftime('%Y/%m/%d')
    uuid_name = uuid4().hex
    return '/'.join(['upload_file_post/', ymd_path, uuid_name])


class Category(models.Model):
    title = models.CharField(max_length=128, unique=True, null=True, blank=False)
    is_anonymous = models.BooleanField(default=False)
    created_date = models.DateTimeField(default=timezone.now)
    top_fixed = models.BooleanField(default=False)
    only_superuser = models.BooleanField(default=False)
    
    creator = models.ForeignKey(User, on_delete=models.CASCADE, related_name="category")
    favorite_user = models.ManyToManyField(User, related_name='favorite_category')
    
    class Meta:
        verbose_name = '게시판 종류'
        verbose_name_plural = '게시판 종류 모음'
        ordering = ['-created_date', ]
        
    def __str__(self):
        return self.title
    

class Post(models.Model):
    title = models.CharField(max_length=128, null=True, blank=False)
    content = models.TextField(default='')
    thumbnail = models.ImageField(upload_to='post_thumbnail/', null=True, blank=True)
    
    hits = models.PositiveIntegerField(default=0)
    created_date = models.DateTimeField(default=timezone.now)
    modified_date = models.DateTimeField(auto_now=True)
    top_fixed = models.BooleanField(default=False)
    
    category = models.ForeignKey(Category, null=True, on_delete=models.CASCADE, related_name="post")
    creator = models.ForeignKey(User, on_delete=models.CASCADE, related_name="post")
    favorite_user = models.ManyToManyField(User, related_name='favorite_post')
    
    class Meta:
        verbose_name = '게시글'
        verbose_name_plural = '게시글 모음'
        ordering = ['-created_date', ]
        
    def __str__(self):
        return self.title


class PostFile(models.Model):
    upload_files = models.FileField(upload_to=get_file_path, null=True, blank=True, verbose_name='파일')
    filename = models.CharField(max_length=64, null=True, verbose_name='첨부파일명')
    
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name="postfile")
    
    class Meta:
        verbose_name = '첨부파일'
        verbose_name_plural = '첨부파일 모음'
        ordering = ['-post__created_date']
    
    def __str__(self):
        return self.filename
    

class Comment(models.Model):
    content = models.TextField(null=True, blank=False)
    created_date = models.DateTimeField(default=timezone.now)
    modified_date = models.DateTimeField(auto_now=True)
    
    creator = models.ForeignKey(User, on_delete=models.CASCADE, related_name="comment")
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name="comment")
    favorite_user = models.ManyToManyField(User, related_name='favorite_comment')
    
    class Meta:
        verbose_name_plural = '댓글'
    
    def __str__(self):
        return self.content
    

class Reply(models.Model):
    content = models.TextField(null=True, blank=False)
    created_date = models.DateTimeField(default=timezone.now)
    modified_date = models.DateTimeField(auto_now=True)
    
    creator = models.ForeignKey(User, on_delete=models.CASCADE, related_name="reply")
    comment = models.ForeignKey(Comment, null=True, on_delete=models.DO_NOTHING, related_name="reply")
    favorite_user = models.ManyToManyField(User, related_name='favorite_reply')
    
    class Meta:
        verbose_name_plural = '대댓글'
    
    def __str__(self):
        return self.content
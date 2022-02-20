import requests, json

from django.db import models
from django.utils.translation import ugettext_lazy as _
from django.contrib.auth import get_user_model

from rest_framework.authentication import get_authorization_header, BasicAuthentication
from rest_framework import HTTP_HEADER_ENCODING

from api.mixins import SafeJWTAuthentication, AdministratorAuthentication

User = get_user_model()

SAFE_METHODS = ('GET', 'HEAD', 'OPTIONS')


class Api(models.Model):
    AUTH_CHOICE_LIST  = (
        (0, _('public auth')),
        (1, _('api auth')),
        (2, _('superuser auth')),
    )
    name = models.CharField(max_length=128)
    description = models.TextField(default="", max_length=500)
    upstream_path = models.CharField(max_length=255)
    upstream_host = models.CharField(max_length=255)
    plugin = models.IntegerField(choices=AUTH_CHOICE_LIST, default=0)
    created = models.DateField(auto_now_add=True)

    def check_auth_perm(self, request):
        
        if self.plugin == 0:
            return True, ''
            
        elif self.plugin == 1:
            auth = SafeJWTAuthentication()
            try:
                user, temp = auth.authenticate(request)
            except:
                return False, 'Authentication credentials were not provided'

            if User.objects.filter(pk=user.id).exists():
                if self.has_permission(user, request.method):
                    return True, ''
                else:
                    return False, 'permission not allowed'
            else:
                return False, 'No user detected'
            
        elif self.plugin == 2:
            auth = AdministratorAuthentication()
            try:
                user, temp = auth.authenticate(request)
            except:
                return False, 'Authentication credentials were not provided'

            if User.objects.filter(pk=user.id).exists():
                if self.has_permission(user, request.method):
                    return True, ''
                else:
                    return False, 'permission not allowed'
            else:
                return False, 'No user detected'
        
        else:
            raise NotImplementedError("plugin %d not implemented" % self.plugin)
    
    def has_permission(self, user, method):
        # IsAuthenticatedOrReadOnly
        return bool(
            method in SAFE_METHODS or
            user and
            user.is_authenticated
        )
    
    def send_request(self, request):
        headers = {}
        headers['Host'] = 'apigateway-service'

        url = self.upstream_host +self.upstream_path
        method = request.method.lower()
        method_map = {
            'get': requests.get,
            'post': requests.post,
            'put': requests.put,
            'patch': requests.patch,
            'delete': requests.delete
        }

        for k,v in request.FILES.items():
            request.data.pop(k)
        
        if request.content_type and request.content_type.lower()=='application/json':
            data = json.dumps(request.data)
            headers['content-type'] = 'application/json; charset=utf-8'
        else:
            data = request.data
        
        
        
        return method_map[method](url, headers=headers, data=data, files=request.FILES)

    def __unicode__(self):
        return self.name

    def __str__(self):
        return self.name
    
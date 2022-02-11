import requests

from django.middleware.security import SecurityMiddleware
from django.conf import settings


class APIGateway:
    def __init__(self, get_response):
        self.get_response = get_response
    
    def __call__(self, request):
        # if hasattr(self, 'process_request'):
        #     request = self.process_request(request)
            
        flag = False
        for func in settings.APIGATEWAY_INNER_FUNCTION:
            if request.path.startswith(func):
                flag = True
                break
        
        if not flag:
            request.path_info = "/api/gateway"
        
        print("path info : ", request.path_info)
        print("origin path : ", request.path)
        # call view
        response = self.get_response(request)

        return response
    
    def process_request(self, request):
        flag = False
        for func in settings.APIGATEWAY_INNER_FUNCTION:
            if request.path.startswith(func):
                flag = True
                break
        
        if not flag:
            request.path_info = "/api/gateway"
        
        return request
        
import requests

from django.middleware.security import SecurityMiddleware
from django.conf import settings


class APIGateway:
    def __init__(self, get_response):
        self.get_response = get_response
    
    def __call__(self, request):
        if hasattr(self, 'process_request'):
            self.process_request(request)
                
        print(request.path_info)
              
        flag = False
        for func in settings.APIGATEWAY_INNER_FUNCTION:
            if request.path.startswith(func):
                flag = True
                break
        
        if not flag:
            request.path_info = "api/gateway"
        
        
        path = request.path
        path = path.split('/')[1:]
        print(f"path : {path}")
        print(self.get_response)
        response = self.get_response(request)

        # 뷰가 호출된 뒤에 실행될 코드들

        return response
    
    def process_request(self, request):
        user_agent = request.META.get('HTTP_USER_AGENT')

        host = request.get_host()
        
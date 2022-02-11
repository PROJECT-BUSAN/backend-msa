import json
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response

from gateway.models import Api
from gateway.tasks import send_request



class Gateway(APIView):
    authentication_classes = ()
    permission_classes = ()
    
    def operation(self, request, *args, **kwargs):
        
        print("In gateway request path : ", request.path)
        path = request.path.split('/')
        
        if len(path) < 2:
            return Response('bad request', status=status.HTTP_400_BAD_REQUEST)
        
        # dangerous logic
        # EX : /api/v1/users...
        path = '/' + path[1] + path[2] + path[3]
        
        apimodel = Api.objects.filter(upstream_path=path)
        if apimodel.count() != 1:
            return Response('bad request', status=status.HTTP_400_BAD_REQUEST)
        
        valid, msg = apimodel.first().check_auth_perm(request)
        if not valid:
            return Response(msg, status=status.HTTP_400_BAD_REQUEST)
        
        response = send_request.delay(request, apimodel)
        response_data = response.get()
        
        # if response.headers.get('Content-Type', '').lower() == 'application/json':
        #     data = response.json()
        # else:
        #     data = response.content
        
        context = {
            "data": response_data
        }
        
        return Response(data=context, status=response.status_code)
    
    
    def get(self, request, *args, **kwargs):
        return self.operation(request, *args, **kwargs)
    
    def post(self, request, *args, **kwargs):
        return self.operation(request, *args, **kwargs)
    
    def put(self, request, *args, **kwargs):
        return self.operation(request, *args, **kwargs)
    
    def put(self, request, *args, **kwargs):
        return self.operation(request, *args, **kwargs)
    
    def delete(self, request, *args, **kwargs):
        return self.operation(request, *args, **kwargs) 
    
    
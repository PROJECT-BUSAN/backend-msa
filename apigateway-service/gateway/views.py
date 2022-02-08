
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response

from gateway.models import Api


class Gateway(APIView):
    
    def operation(self, request, *args, **kwargs):
        path = request.path_info.split('/')
        if len(path) < 2:
            return Response('bad request', status=status.HTTP_400_BAD_REQUEST)
        
        apimodel = Api.objects.filter(up=path[3])
        if apimodel.count() != 1:
            return Response('bad request', status=status.HTTP_400_BAD_REQUEST)
        
        valid, msg = apimodel.first().check_auth_perm(request)
        if not valid:
            return Response(msg, status=status.HTTP_400_BAD_REQUEST)
        
        response = apimodel[0].send_request(request)
        if response.headers.get('Content-Type', '').lower() == 'application/json':
            data = response.json()
        else:
            data = response.content
        return Response(data=data, status=response.status_code)
    
    
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
    
    
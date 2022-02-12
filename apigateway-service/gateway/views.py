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
        upstream_path = request.path
        path = request.path.split('/')
        
        if len(path) < 2:
            return Response('Bad request Path', status=status.HTTP_400_BAD_REQUEST)
        
        # ** Most Dangerous Code **
        # EX : /api/v1/users...
        path = '/' + path[1] + "/" + path[2] + "/" + path[3]
        
        apimodel = Api.objects.filter(upstream_path=path)
        if apimodel.count() != 1:
            return Response('No Reserved API. Please Register API Host and Path you are Using', status=status.HTTP_400_BAD_REQUEST)
        
        apimodel = apimodel.first()
        valid, msg = apimodel.check_auth_perm(request)
        if not valid:
            return Response(msg, status=status.HTTP_400_BAD_REQUEST)
        
        request = {
            "method": request.method,
            "data": request.data,
            "content-type": request.content_type,
            "files": request.FILES,
        }
        
        response = send_request.delay(request, apimodel.upstream_host, upstream_path)
        response_data = response.get()
        
        # if response.headers.get('Content-Type', '').lower() == 'application/json':
        #     data = response.json()
        # else:
        #     data = response.content
        
        data = {
            "data": response_data,
        }
        context = []
        context.append(response_data)
        
        return Response(data=context)
    
    
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
    
    
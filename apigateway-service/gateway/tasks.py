from __future__ import absolute_import, unicode_literals
import json, requests

from celery import shared_task

from gateway.models import Api


@shared_task
def send_request(request, obj:Api):
    headers = {}
    headers['Host'] = 'apigateway-service'

    url = "http://" + obj.upstream_host + obj.upstream_path
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
        headers['Content-Type'] = 'application/json; charset=utf-8'
    else:
        data = request.data
    
    response = method_map[method](url, headers=headers, data=data, files=request.FILES)
    
    response_content = response.content.decode('utf-8')
    response_data = json.loads(response_content)
    return response_data


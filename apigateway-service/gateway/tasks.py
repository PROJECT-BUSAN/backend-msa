from __future__ import absolute_import, unicode_literals
import json, requests

from celery import shared_task


@shared_task
def send_request(request, *args, **kwargs):
    
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    response = requests.post("http://profile-service:8001/api/",
                                data=json.dumps(data), headers=headers)
    
    response_content = response.content.decode('utf-8')
    message = json.loads(response_content)["message"]
    return message

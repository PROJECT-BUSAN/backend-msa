import json, requests

from rest_framework.test import APIClient, APITestCase

from gateway.models import Api


class UserTest(APITestCase):
    client = APIClient()
    headers = {}
    
    def setUp(self):
        api = Api(
            name="profile",
            description="프로필 관련 서버",
            upstream_path="/api/v1/profile",
            upstream_host="profile-service",
            plugin=0,
        )
        api.save()
    
    def test_gateway_routing(self):
        path = "/api/v1/profile/3"
        
        print("Before requests")
        response = self.client.get(
            path, content_type='application/json'
        )
        print("After requests")
        
        print(response)
    
    
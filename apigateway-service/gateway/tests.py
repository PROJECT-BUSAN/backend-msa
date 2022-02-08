import json, requests

from rest_framework.test import APIClient, APITestCase



class UserTest(APITestCase):
    client = APIClient()
    headers = {}
    
    def test_create_user(self):
        
        data = {
            
        }
        
        response = requests.post(
            '/api/v1/users/me/', json.dumps(data), content_type='application/json'
        )
        self.assertEqual(response.status_code, 200)
    
    
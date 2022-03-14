import json
from django.core.exceptions import ValidationError

from rest_framework.test import APITestCase, APIClient

from users.models import User, Profile


class AuthTest(APITestCase):
    client = APIClient()
    headers = {}
    
    def setUp(self):
        data = {
            'username': "test",
            'email': "test@test.com",
            'password1': "test",
            'last_name': "a",
            'first_name': "b",
            'gender': "M"
        }
        user = User.objects.create_user(data)
        self.user = user
        
        
    def test_login_api(self):
        print("test_login_api")
        user = {
            'username': 'test',
            'password': 'test'
        }

        response = self.client.post('/api/v1/auth/login', json.dumps(user), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
    
    def test_username_duplicate_api(self):
        print("test_username_duplicate_api")
        context = {
            'username': 'test',
        }
        response = self.client.post('/api/v1/auth/validate/username', json.dumps(context), content_type='application/json')
        self.assertEqual(response.status_code, 403)
        
        context = {
            'username': 'test1',
        }
        response = self.client.post('/api/v1/auth/validate/username', json.dumps(context), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
        
    def test_email_duplicate_api(self):
        print("test_email_duplicate_api")
        context = {
            'email': 'test@test.com',
        }
        response = self.client.post('/api/v1/auth/validate/email', json.dumps(context), content_type='application/json')
        self.assertEqual(response.status_code, 403)
        
        context = {
            'email': 'test1@test.com',
        }
        response = self.client.post('/api/v1/auth/validate/email', json.dumps(context), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
    
    def test_login_api_get_token(self):
        print("test_login_api_get_token")
        context = {
            "username": "test",
            "password": "test"
        }
        
        response = self.client.post('/api/v1/auth/login', json.dumps(context), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
        self.token = response.data['access_token']
        self.csrftoken = response.cookies.get('csrftoken').value
        
        self.assertNotEqual(self.token, '')
        self.assertNotEqual(self.csrftoken, '')
        
        # print("jwt_token : ", self.token)
        # print("csrf_token : ", self.csrftoken)
    
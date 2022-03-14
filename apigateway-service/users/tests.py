import json

from rest_framework.test import APIClient, APITestCase

from users.models import User, Profile


class UserTest(APITestCase):
    client = APIClient()
    headers = {}
    
    def setUp(self):
        data = {
            'username': "TEST",
            'email': "test@nav.com",
            'password1': "test1234@",
            'last_name': "a",
            'first_name': "b",
            'gender': "M"
        }
        user = User.objects.create_user(data)
        self.user = user
        
        user = {
            "username": "TEST",
            "password": "test1234@"
        }
        
        response = self.client.post('/api/v1/auth/login', json.dumps(user), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
        self.token = response.data['access_token']
        self.csrftoken = response.cookies.get('csrftoken').value
        
        self.assertNotEqual(self.token, '')
        
        self.headers = {
            "HTTP_Authorization": "jwt " + self.token,
            "X-CSRFToken": self.csrftoken,
        }
        
    
    def test_create_user(self):
        '''
        기본 루트 회원가입 API 테스트
        '''
        
        userdata = {
            "username": "testuser",
            "password1": "@qwerty1234",
            "password2": "@qwerty1234",
            "last_name": "테",
            "first_name": "스트",
            "gender": "M",
            "email": "test131@test.com"
        }
        
        response = self.client.post(
            '/api/v1/users/new', json.dumps(userdata), **self.headers, content_type='application/json'
        )
        self.assertEqual(response.status_code, 200)
        
        user = User.objects.filter(username="testuser").first()
        self.assertIsInstance(user, User)
        
    
    def test_create_super_user(self):
        '''
        관리자 생성 테스트
        '''
        user = User.objects.create_superuser('admin', 'admin@admin.com', 'admin1234@')
        self.assertIsInstance(user, User)
        self.assertTrue(user.is_staff)
        self.assertEqual(user.email, 'admin@admin.com')
        
    
    def test_change_password(self):
        '''
        현재 유저 비밀번호 변경 API 테스트
        '''
        context = {
            "newpassword1": "test12345@",
            "newpassword2": "test12345@",
        }
        
        response = self.client.put(
            '/api/v1/users/password',
            json.dumps(context), **self.headers, content_type="application/json")
        self.assertEqual(response.status_code, 200)
    
    def test_change_password(self):
        '''
        현재 유저 비밀번호 변경 API 테스트(이전 비밀번호와 동일 에러 처리)
        '''
        context = {
            "newpassword1": "test1234@",
            "newpassword2": "test1234@",
        }
        
        response = self.client.put(
            '/api/v1/users/password',
            json.dumps(context), **self.headers, content_type="application/json")
        self.assertEqual(response.status_code, 400)
    
    def test_user_delete(self):
        '''
        현재 유저 회원 탈퇴 API 테스트
        '''
        context = {
            "password": "test1234@"
        }
        
        response = self.client.delete(
            '/api/v1/users',
            json.dumps(context), **self.headers, content_type="application/json")
        self.assertEqual(response.status_code, 204)
    
    
class ProfileTest(APITestCase):
    client = APIClient()
    headers = {}
    
    def setUp(self):
        data = {
            'username': "TEST",
            'email': "test@nav.com",
            'password1': "test1234@",
            'last_name': "a",
            'first_name': "b",
            'gender': "M"
        }
        user = User.objects.create_user(data)
        self.user = user
        
        user = {
            "username": "TEST",
            "password": "test1234@"
        }
        
        response = self.client.post('/api/v1/auth/login', json.dumps(user), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        
        self.token = response.data['access_token']
        self.csrftoken = response.cookies.get('csrftoken').value
        
        self.assertNotEqual(self.token, '')
        
        self.headers = {
            "HTTP_Authorization": "jwt " + self.token,
            "X-CSRFToken": self.csrftoken,
        }
        
    def test_info_api(self):
        response = self.client.get(
            '/api/v1/users',
            **self.headers, content_type = "application/json")
        self.assertEqual(response.status_code, 200)
        

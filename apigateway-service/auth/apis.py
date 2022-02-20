import jwt

from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response

from django.contrib.auth import get_user_model
from django.conf import settings
from django.views.decorators.csrf import csrf_protect, ensure_csrf_cookie

from auth.authenticate import generate_access_token, jwt_login
from api.mixins import PublicApiMixin
from django.utils.decorators import method_decorator


User = get_user_model()


@method_decorator(ensure_csrf_cookie, name="dispatch")
class LoginApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        username 과 password를 가지고 login 시도
        key값 : username, password
        """
        user = User
        username = request.data.get('username')
        password = request.data.get('password')
        
        if (username is None) or (password is None):
            return Response({
                "message": "username/password required"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        user = User.objects.filter(username=username).first()
        if user is None:
            return Response({
                "message": "유저를 찾을 수 없습니다"
            }, status=status.HTTP_404_NOT_FOUND)
        if not user.check_password(password):
            return Response({
                "message": "wrong password"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        response = Response(status=status.HTTP_200_OK)
        return jwt_login(response, user)
        

@method_decorator(csrf_protect, name='dispatch')
class RefreshJWTtoken(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        refresh_token = request.COOKIES.get('refreshtoken')
        
        if refresh_token is None:
            return Response({
                "message": "Authentication credentials were not provided."
            }, status=status.HTTP_403_FORBIDDEN)
        
        try:
            payload = jwt.decode(
                refresh_token, settings.REFRESH_TOKEN_SECRET, algorithms=['HS256']
            )
        except:
            return Response({
                "message": "expired refresh token, please login again."
            }, status=status.HTTP_403_FORBIDDEN)
        
        user = User.objects.filter(id=payload['user_id']).first()
        
        if user is None:
            return Response({
                "message": "user not found"
            }, status=status.HTTP_400_BAD_REQUEST)
        if not user.is_active:
            return Response({
                "message": "user is inactive"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        access_token = generate_access_token(user)
        
        return Response(
            {
                'access_token': access_token,
            }
        )
        
        
@method_decorator(csrf_protect, name='dispatch')
class LogoutApi(PublicApiMixin, APIView):
    def post(self, request):
        """
        클라이언트 refreshtoken 쿠키를 삭제함으로 로그아웃처리
        """
        response = Response({
            "message": "Logout success"
            }, status=status.HTTP_202_ACCEPTED)
        response.delete_cookie('refreshtoken')

        return response


@method_decorator(csrf_protect, name='dispatch')
class username_duplicate_checkApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        회원가입 과정에서 username 중복확인 api
        'username'값 전송 필요
        """
        input_username = request.data.get('username', '')
        
        if not input_username:
            return Response({
                "message": "Need username"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        user = User.objects.filter(username=input_username)
        
        if user.exists():
            return Response({
                "message": "There is an ID registered with that username"
            }, status=status.HTTP_403_FORBIDDEN)
        
        return Response({
            "message": "Allowed username"
        },status=status.HTTP_200_OK)


@method_decorator(csrf_protect, name='dispatch')
class email_duplicate_checkApi(PublicApiMixin, APIView):
    def post(self, request, *args, **kwargs):
        """
        회원가입 과정에서 email 중복확인 api
        'email' 값 전송 필요
        """
        input_email = request.data.get('email', '')
        
        if not input_email:
            return Response({
                "message":"Need email"
            }, status=status.HTTP_400_BAD_REQUEST)
        
        user = User.objects.filter(email=input_email)
        
        if user.exists():
            return Response({
                "message": "There is an ID registered with that email"
            }, status=status.HTTP_403_FORBIDDEN)
        
        return Response({
            "message": "Allowed email"
        },status=status.HTTP_200_OK)

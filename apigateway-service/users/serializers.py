from rest_framework import serializers

from django.utils.translation import gettext_lazy as _
from django.contrib.auth import get_user_model

from users.models import Profile


User = get_user_model()


def validate_password12(password1, password2):
    validate_condition = [
        lambda s: all(x.islower() or x.isupper() or x.isdigit() or (x in ['!', '@', '#', '$', '%', '^', '&', '*', '_']) for x in s), ## 영문자 대소문자, 숫자, 특수문자(리스트)만 허용
        # lambda s: any(x.islower() or x.isupper() for x in s), ## 영어 대소문자 필수
        # lambda s: any((x in ['!', '@', '#', '$', '%', '^', '&', '*', '_']) for x in s), ## 특수문자 필수
        # lambda s: len(s) == len(s.replace(" ","")),
        # lambda s: len(s) >= 6, ## 글자수 제한
        lambda s: len(s) <= 20, ## 글자수 제한
    ]

    for validator in validate_condition:
        if not validator(password1):
            raise serializers.ValidationError(
            _("password ValidationError")
        )
    
    if not password1 or not password2:
        raise serializers.ValidationError(
            _("need two password fields")
        )
    if password1 != password2:
        raise serializers.ValidationError(
            _("password fields didn't match!"))
    
    return password1


class ProfileSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = Profile
        fields = [
            'nickname',
            'realname',
            'image',
            'introduce',
            'is_project',
            'signup_path',
        ]
        

class UserSerializer(serializers.ModelSerializer):
    profile = ProfileSerializer(read_only=True)
    
    class Meta:
        model = User
        fields = [
            'id',
            'username',
            'is_superuser',
            'email',
            'profile',
            'last_login',
            'date_joined',
        ]


class RegisterSerializer(serializers.Serializer):
    username = serializers.CharField(max_length=150, write_only=True)
    first_name = serializers.CharField(max_length=150, write_only=True)
    last_name = serializers.CharField(max_length=150, write_only=True)
    
    gender = serializers.CharField(max_length=8, write_only=True)
    email = serializers.EmailField(write_only=True)
    password1 = serializers.CharField(write_only=True)
    password2 = serializers.CharField(write_only=True)

    def validate_email(self, email):
        if not email:
            raise serializers.ValidationError(
                _("email field not allowed empty")
            )
        used = User.objects.filter(email__iexact=email)
        if used.count() > 0:
            raise serializers.ValidationError(
                _("A user is already registered with this e-mail address."))
        
        return email
    
    def validate_username(self, username):
        validate_condition = [
            lambda s: all(x.islower() or x.isdigit() or '_' for x in s), ## 영문자 대소문자, 숫자, 언더바(_)만 허용
            lambda s: any(x.islower() for x in s), ## 영어 소문자 필수
            lambda s: len(s) == len(s.replace(" ","")), ## 공백 제한
            lambda s: len(s) >= 3, ## 글자수 제한
            lambda s: len(s) <= 20, ## 글자수 제한
        ]

        for validator in validate_condition:
            if not validator(username):
                raise serializers.ValidationError(
                    _("username ValidationError")
                )
        
        if not username:
            raise serializers.ValidationError(
                _("username field not allowed empty")
            )
        
        used = User.objects.filter(username__iexact=username).first()
        if used:
            raise serializers.ValidationError(
                _("A user is already registered with this username."))
        
        return username
    
    def validate(self, data):
        data['password1'] = validate_password12(data['password1'], data['password2'])
        data['email'] = self.validate_email(data['email'])
        data['username'] = self.validate_username(data['username'])
        return data

    def create(self, validated_data):
        user = User.objects.create_user(validated_data)
        
        return user


class PasswordChangeSerializer(serializers.Serializer):
    newpassword1 = serializers.CharField(write_only=True)
    newpassword2 = serializers.CharField(write_only=True)
    
    def validate_password(self, newpassword1, newpassword2):
        newpassword = validate_password12(newpassword1, newpassword2)
        
        return newpassword
    
    def validate(self, data):
        data['newpassword1'] = self.validate_password(
            data['newpassword1'], data['newpassword2'])
        
        return data
    
    def update(self, user, validated_data):
        user.set_password(validated_data.get('newpassword1'))
        user.save()
        
        return user
    
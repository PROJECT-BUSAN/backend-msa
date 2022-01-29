from django.contrib.auth.base_user import BaseUserManager
from django.contrib.auth.models import AbstractUser
from django.core import validators
from django.db import models, transaction

from django.utils.translation import gettext_lazy as _
from django.utils.deconstruct import deconstructible

from boards.models import Post, Category, Comment, Reply


@deconstructible
class UnicodeUsernameValidator(validators.RegexValidator):
    regex = r'^[\w.@+-]+\Z'
    message = _(
        'Enter a valid username. This value may contain only letters, '
        'numbers, and @/./+/-/_ characters.'
    )
    flags = 0


class UserManager(BaseUserManager):
    @transaction.atomic
    def create_user(self, validate_data):
        username = validate_data["username"]
        email = validate_data["email"]
        password = validate_data["password1"]
        first_name = validate_data['first_name']
        last_name = validate_data['last_name']
        
        if not username:
            raise ValueError('아이디는 필수 항목입니다.')
        if not email:
            raise ValueError('이메일은 필수 항목입니다.')
        if not password:
            raise ValueError('패드워드는 필수 항목입니다.')
        
        
        user = self.model(
            username=username,
            first_name=first_name,
            last_name=last_name,
            email = self.normalize_email(email)
        )
        user.set_password(password)
        user.full_clean()
        user.save()
        profile = Profile.create_profile(self, user=user, data=validate_data)
        profile.save()
        
        return user
    
    def create_superuser(self, username, email=None, password=None):
        
        user = self.model(
            username=username,
            email = self.normalize_email(email)
        )
        user.set_password(password)
        user.full_clean()
        user.is_admin = True
        user.is_superuser = True
        user.is_staff = True
        user.save()
        
        profile = Profile.create_profile(self, user=user, data={})
        profile.save()
    
        return user


class User(AbstractUser):
    username_validator = UnicodeUsernameValidator()

    username = models.CharField(
        _('username'),
        max_length=150,
        unique=True,
        help_text=_('Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.'),
        validators=[username_validator],
        error_messages={
            'unique': _("A user with that username already exists."),
        },
    )
    email = models.EmailField(
        _('email address'),
        unique=True,
        blank=True
    )
    
    objects = UserManager()
    
    class Meta:
        swappable = 'AUTH_USER_MODEL'
        
    def __str__(self):
        return self.username
    

    @property
    def name(self):
        if not self.last_name:
            return self.first_name.capitalize()

        return f'{self.first_name.capitalize()} {self.last_name.capitalize()}'


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name="profile")
    auth = models.CharField(max_length=128, null=True, blank=True)
    realname = models.CharField(max_length=64, null=True, blank=True)
    nickname = models.CharField(max_length=64, unique=True, null=True, blank=True)
    image = models.ImageField(
        default='profile_image/basic_profile.png',
        upload_to='profile_image/',
        null=True, blank=True
    )
    introduce = models.TextField(null=True, blank=True)
    GENDER_CHOICE = (
        ("M", "남자"),
        ("F", "여자")
    )
    gender = models.CharField(max_length=8, null=True, choices=GENDER_CHOICE)
    
    is_project = models.BooleanField(default=False)
    signup_path = models.CharField(max_length=64, default='basic')
    
    def __str__(self):
        return self.user.username
    
    def create_profile(self, user, data):
        last_name = data.get("last_name", '')
        first_name = data.get("first_name", '')
        gender = data.get("gender", '')
        
        profile = Profile(
            user=user,
            realname = last_name + first_name,
            gender=gender
        )
        profile.save()
        
        return profile

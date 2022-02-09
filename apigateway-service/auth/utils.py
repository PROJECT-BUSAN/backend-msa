from django.db import transaction
from django.utils import timezone
from django.contrib.auth import get_user_model

from users.models import Profile

User = get_user_model()

@transaction.atomic
def social_user_create(username, password=None, **extra_fields):
    user = User(username=username, email=username)
    
    if password:
        user.set_password(password)
    else:
        user.set_unusable_password()
        
    user.full_clean()
    user.save()
    
    profile = Profile(user=user)
    
    try:
        profile.image = extra_fields['image']
    except:
        pass
    
    if extra_fields['nickname'] == '':
       profile.nickname = username
    else:
       profile.nickname = extra_fields['nickname']
    
    try:
        try:
            user.first_name = extra_fields['first_name']
            user.last_name = extra_fields['last_name']
        except:
            try:
                user.first_name = extra_fields['name']
            except:
                pass
    except:
        pass
    
    try:
        path = extra_fields['path']
        profile.signup_path = f"{path}"
        profile.image = f"profile_image/{path}_basic.png"
    except:
        pass
    
    profile.save()
    
    return user


@transaction.atomic
def social_user_get_or_create(username, **extra_data):
    user = User.objects.filter(email=username).first()
    
    if user:
        return user, False
    
    return social_user_create(username=username, **extra_data), True

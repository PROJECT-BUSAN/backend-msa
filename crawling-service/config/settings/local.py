from .base import *

DEBUG = True

ALLOWED_HOSTS = ['*']

SECRET_KEY = 'django-insecure-8%z(f@r0oelt(@5!s8q*^&d9!fxc#6a3r8h)(b_#cg78p(ntfn'

BASE_BACKEND_URL = 'http://localhost:8000'
BASE_FRONTEND_URL = 'http://localhost:3000'

INTERNAL_IPS = [
    '127.0.0.1',
]

# CORS SETTINGS
CORS_ORIGIN_ALLOW_ALL = True
CORS_ALLOW_CREDENTIALS = True

# Database
# https://docs.djangoproject.com/en/3.2/ref/settings/#databases
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}


# Integral Test
# DATABASES = {
#     'default': {
#         'ENGINE': 'django.db.backends.mysql',
#         'NAME': 'investment-db',
#         'USER': 'user',
#         'PASSWORD': '1234',
#         'HOST': 'investment-database',
#         'PORT': '3307',
#     }
# }

DATA_UPLOAD_MAX_NUMBER_FIELDS = 4000

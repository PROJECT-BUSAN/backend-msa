from .base import *


# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

ALLOWED_HOSTS = ['*']

SECRET_KEY = 'django-insecure-ndhi2hjc5pe+0j-(t9jhyq7)lxvq*=-xvi^hb2sf_ry_pqa4q3'

# django_debug_tool
INTERNAL_IPS = [
    '127.0.0.1',
]

BASE_BACKEND_URL = 'http://localhost:8000'
BASE_FRONTEND_URL = 'http://localhost:3000'

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

# LOGGING = {
#     'version': 1,
#     'disable_existing_loggers': False,
#     'filters': {
#         'require_debug_false': {
#             '()': 'django.utils.log.RequireDebugFalse',
#         },
#         'require_debug_true': {
#             '()': 'django.utils.log.RequireDebugTrue',
#         },
#     },
#     'formatters': {
#         'django.server': {
#             '()': 'django.utils.log.ServerFormatter',
#             'format': '[{server_time}] {message}',
#             'style': '{',
#         },
#         'standard': {
#             'format': '%(asctime)s [%(levelname)s] %(name)s: %(message)s'
#         },
#     },
#     'handlers': {
#         'console': {
#             'level': 'INFO',
#             'filters': ['require_debug_true'],
#             'class': 'logging.StreamHandler',
#         },
#         'django.server': {
#             'level': 'INFO',
#             'class': 'logging.StreamHandler',
#             'formatter': 'django.server',
#         },
#         'mail_admins': {
#             'level': 'ERROR',
#             'filters': ['require_debug_false'],
#             'class': 'django.utils.log.AdminEmailHandler'
#         },
#         'file': {
#             'level': 'INFO',
#             'encoding': 'utf-8',
#             'filters': ['require_debug_true'],
#             'class': 'logging.handlers.TimedRotatingFileHandler',
#             'when': 'midnight', # 매 자정마다
#             'filename': BASE_DIR / 'logs/apigateway.log',
#             # 'maxBytes': 1024*1024*5,  # 5 MB
#             'backupCount': 14,
#             'formatter': 'standard',
#         },
#     },
#     'loggers': {
#         'django': {
#             'handlers': ['console', 'mail_admins', 'file'],
#             'level': 'INFO',
#         },
#         'django.server': {
#             'handlers': ['django.server'],
#             'level': 'INFO',
#             'propagate': False,
#         },
#         'project': {
#             'handlers': ['console', 'file'],
#             'level': 'INFO',
#         },
#     }
# }
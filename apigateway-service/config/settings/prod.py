from .base import *


DEBUG = False


BASE_BACKEND_URL = env.str('DJANGO_BASE_BACKEND_URL')
BASE_FRONTEND_URL = env.str('DJANGO_BASE_FRONTEND_URL')


# CORS SETTINGS
CORS_ORIGIN_ALLOW_ALL = True
CORS_ALLOW_CREDENTIALS = True
# CORS_ORIGIN_WHITELIST = env.list(
#     'DJANGO_CORS_ORIGIN_WHITELIST',
#     default=[BASE_FRONTEND_URL]
# )

# 실제 배포시 바꿀 예정
ALLOWED_HOSTS = ['buscp.org']

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'projectdb',
        'USER': 'project',
        'PASSWORD': env.str('PROJECT_MARIADB_PASSWORD'),
        'HOST': 'projectmariadb',
        'PORT': '3306',
    }
}

DATA_UPLOAD_MAX_NUMBER_FIELDS = 10000


LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'filters': {
        'require_debug_false': {
            '()': 'django.utils.log.RequireDebugFalse',
        },
        'require_debug_true': {
            '()': 'django.utils.log.RequireDebugTrue',
        },
    },
    'formatters': {
        'django.server': {
            '()': 'django.utils.log.ServerFormatter',
            'format': '[{server_time}] {message}',
            'style': '{',
        },
        'standard': {
            'format': '%(asctime)s [%(levelname)s] %(name)s: %(message)s'
        },
    },
    'handlers': {
        'console': {
            'level': 'INFO',
            'filters': ['require_debug_false'],
            'class': 'logging.StreamHandler',
        },
        'django.server': {
            'level': 'INFO',
            'class': 'logging.StreamHandler',
            'formatter': 'django.server',
        },
        'mail_admins': {
            'level': 'ERROR',
            'filters': ['require_debug_false'],
            'class': 'django.utils.log.AdminEmailHandler'
        },
        'file': {
            'level': 'INFO',
            'encoding': 'utf-8',
            'filters': ['require_debug_false'],
            'class': 'logging.handlers.TimedRotatingFileHandler',
            'when': 'midnight', # 매 자정마다
            'filename': BASE_DIR / 'logs/apigateway.log',
            # 'maxBytes': 1024*1024*5,  # 5 MB
            'backupCount': 14,
            'formatter': 'standard',
        },
    },
    'loggers': {
        'django': {
            'handlers': ['console', 'mail_admins', 'file'],
            'level': 'INFO',
        },
        'django.server': {
            'handlers': ['django.server'],
            'level': 'INFO',
            'propagate': False,
        },
        'project': {
            'handlers': ['console', 'file'],
            'level': 'INFO',
        },
    }
}
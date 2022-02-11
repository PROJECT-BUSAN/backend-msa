from django.urls import path

from gateway.views import Gateway


app_name = 'gateway'


urlpatterns = [
    path('gateway', Gateway.as_view()),
    
]

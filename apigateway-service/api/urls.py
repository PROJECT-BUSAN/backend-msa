from django.urls import path, include


v1_patterns = [
    path('auth/', include('auth.urls')),
    path('users/', include('users.urls')),
    
]

urlpatterns = [
    path('v1/', include(v1_patterns)),
    path('gateway', include('gateway.urls')),
]

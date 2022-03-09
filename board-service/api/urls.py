from django.urls import path, include


v1_patterns = [
    path('board/', include('boards.urls')),
    path('log/', include('logapp.urls')),
    
]

urlpatterns = [
    path('v1/', include(v1_patterns)),
    path('', include('swagger.urls')),
    path('summernote/', include('django_summernote.urls')),
]

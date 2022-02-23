from django.urls import path, include


v1_patterns = [
    path('crawling', include('stockapp.urls')),
    
]

    
urlpatterns = [
    path('v1/', include(v1_patterns)),
    
]

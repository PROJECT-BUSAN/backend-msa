from django.urls import path, include


v1_patterns = [
    path('auth/', include('auth.urls')),
    path('users/', include('users.urls')),
    path('board/', include('boards.urls')),
    path('activity/', include('activity.urls')),
    path('faqs/', include('FAQs.urls')),
    path('reservations/', include('reservations.urls')),
    path('survey/', include('survey.urls')),
    path('log/', include('logapp.urls')),
    
]

sotonggori = [
    path('', include('sotongapp.urls')),
]

urlpatterns = [
    path('v1/', include(v1_patterns)),
    path('sotong/', include(sotonggori)),
    path('', include('swagger.urls')),
    path('summernote/', include('django_summernote.urls')),
]

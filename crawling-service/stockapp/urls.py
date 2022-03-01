from django.urls import path, include

from stockapp.views import CrawlingView

    
urlpatterns = [
    path('', CrawlingView.as_view()),
    
]

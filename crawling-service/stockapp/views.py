from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView

from stockapp.services import Crawling


class CrawlingView(APIView):
    def post(self, request, *args, **kwargs):
        try:
            crawling = Crawling()
            crawling.run()
            
        except:
            return Response(
                {"message":"Crawling Error. Crawling's run method need to check"}
                , status=status.HTTP_503_SERVICE_UNAVAILABLE)
        
        return Response(
                {"data":"Crawling Success"}
                , status=status.HTTP_200_OK)

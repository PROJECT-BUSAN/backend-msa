from django.contrib import admin

from stockapp.models import Company, StockInfo


admin.site.register(Company)
admin.site.register(StockInfo)

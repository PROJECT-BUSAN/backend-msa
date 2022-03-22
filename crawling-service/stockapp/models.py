from django.db import models


class Company(models.Model):
    company_id = models.BigAutoField(primary_key=True, db_column="company_id")
    stock_name = models.CharField(help_text="종목명", max_length=200, null=True, blank=True, db_column="stock_name")
    stock_code = models.CharField(help_text="종목코드", max_length=200, null=True, blank=True, db_column="stock_code")
    
    def __str__(self):
        return self.stock_name
    
    class Meta:
        db_table = "company"
        

class StockInfo(models.Model):
    id = models.BigAutoField(primary_key=True, db_column="id")

    # 소켓 메시지에서 LocalTime 형식이 변환되지 않아 Char 타입으로 변경
    date = models.CharField(help_text="거래일", max_length=32, blank=True, null=True, db_column="date")
    close = models.FloatField(help_text="종가", blank=True, null=True, db_column="close")
    open = models.FloatField(help_text="시가", blank=True, null=True, db_column="open")
    high = models.FloatField(help_text="고가", blank=True, null=True, db_column="high")
    low = models.FloatField(help_text="저가", blank=True, null=True, db_column="low")
    volume = models.IntegerField(help_text="거래량", blank=True, null=True, db_column="volume")
    
    company = models.ForeignKey(Company, on_delete=models.CASCADE, related_name='stockinfo')
    
    def __str__(self):
        return str(self.company) + " - " + str(self.date)
    
    class Meta:
        db_table = "stockinfo"

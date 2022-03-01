import time
from pykrx import stock
from datetime import datetime

from stockapp.models import Company, StockInfo


class Crawling:
    stock_codes = []
    
    def __init__(self):
        self.date = datetime.today().date().strftime("%Y%m%d")
    
    '''
    start_date ~ end_date 기간에 code에 해당하는 기업의 주가를 크롤링한다.
    '''
    def daily(self, start_date, end_date):
        companies = Company.objects.filter().all()
        for com in companies:
            code = com.stock_code
            df_ohlcv = stock.get_market_ohlcv_by_date(start_date, end_date, code)
            for row in df_ohlcv.itertuples():
                obj, flag = StockInfo.objects.get_or_create(
                    date=row[0].to_pydatetime().date(),
                    open=row[1],
                    high=row[2],
                    low=row[3],
                    close=row[4],
                    volume=row[5],
                    company=com
                )
            
            time.sleep(0.5)
            

    '''
    date 기준으로 코스피, 코스닥에 상장된 기업의 이름과 종목코드를 가져온다.
    '''
    def company(self, date):
        # market = "KOSPI" : 코스피 상장기업
        # market = "KOSDAQ" : 코스닥 상장기업
        self.stock_codes = stock.get_market_ticker_list(date, market="ALL")
        for stock_code in self.stock_codes:
            stock_name = stock.get_market_ticker_name(stock_code);
            obj, flag = Company.objects.get_or_create(
                stock_name=stock_name,
                stock_code=stock_code
            )
            
    
    def run(self):
        self.company(self.date)
        self.daily("20150101", "20220201")
        
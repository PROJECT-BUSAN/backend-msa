FROM python:3.9.0

RUN apt-get upgrade && pip3 install --upgrade pip

WORKDIR /home/crawling

ADD ./requirements.txt /home/crawling/requirements.txt

RUN pip install -r requirements.txt

COPY ./ /home/crawling

RUN pip install gunicorn && pip install mysqlclient

EXPOSE 8001
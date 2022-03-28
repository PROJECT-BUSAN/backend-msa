FROM python:3.9.0

RUN apt-get upgrade && \
    pip3 install --upgrade pip

WORKDIR /home/apigateway

ADD ./requirements.txt /home/apigateway/requirements.txt

RUN pip install -r requirements.txt && \
    pip install gunicorn && \
    pip install mysqlclient

COPY ./ /home/apigateway

EXPOSE 8000

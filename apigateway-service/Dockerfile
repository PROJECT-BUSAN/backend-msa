FROM python:3.9.0

COPY ./ /home/apiagteway

WORKDIR /home/apiagteway

RUN apt-get upgrade && pip3 install --upgrade pip

RUN pip install -r requirements.txt

RUN pip install gunicorn

# RUN pip install mysqlclient

EXPOSE 8000

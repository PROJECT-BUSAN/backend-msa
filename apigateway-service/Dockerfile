FROM python:3.9.0

COPY ./ /home/ubuntu/backend-msa/apiagteway

WORKDIR /home/ubuntu/backend-msa/apiagteway

RUN apt-get upgrade && pip3 install --upgrade pip

RUN pip install -r requirements.txt

RUN pip install gunicorn

# RUN pip install mysqlclient

EXPOSE 8000

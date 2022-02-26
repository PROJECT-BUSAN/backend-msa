#!/bin/bash

docker build -t apigateway-service  ./apigateway-service/

# docker build -t profile-service  ./profile-service/profile-service/

docker build -t crawling-service  ./crawling-service/

docker build -t investment-service ./investment-service/
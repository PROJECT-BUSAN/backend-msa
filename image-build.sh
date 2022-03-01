#!/bin/bash

docker build -t apigateway  ./apigateway-service/

docker build -t profile-service  ./profile-service/profile-service/

# docker build -t crawling  ./crawling-service/

# docker build -t investment-service ./investment-service/
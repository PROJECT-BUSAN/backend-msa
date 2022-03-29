#!/bin/bash

docker build -t apigateway ./apigateway-service/

docker build -t crawling ./crawling-service/

# cd ./profile-service/profile-service/
# ./gradlew build
# docker build -t profile-service .
# cd ../..

cd ./investment-service/
./gradlew build
docker build -t investment-service .
cd ..
#!/bin/bash

cd ./apigateway-service
docker build -t apigateway  .
cd ..

cd ./profile-service/profile-service
docker build -t profile-service  .
cd ../..

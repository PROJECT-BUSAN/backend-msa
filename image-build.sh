#!/bin/bash

docker build -t profile ./profile-service/profile-service/
echo "profile image build success!\n"

docker build -t apigateway ./apigateway-service/
echo "apigateway image build success!\n"

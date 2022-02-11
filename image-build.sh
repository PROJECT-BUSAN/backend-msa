#!/bin/bash

docker build -t apigateway -f ./apigateway-service/Dockerfile .

docker build -t profile-service -f ./profile-service/profile-service/Dockerfile .

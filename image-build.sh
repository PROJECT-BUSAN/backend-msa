#!/bin/bash

docker build -t apigateway -f ./apigateway-service

docker build -t profile-service -f ./profile-service/profile-service

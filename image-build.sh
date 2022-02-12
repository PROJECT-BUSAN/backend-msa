#!/bin/bash

docker build -t apigateway  ./apigateway-service/

docker build -t profile-service  ./profile-service/profile-service/

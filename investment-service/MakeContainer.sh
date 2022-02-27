#!/bin/bash

docker build -t investment-service .

docker-compose up -d

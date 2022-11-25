#!/bin/bash

./gradlew build

docker build -t profile-service .
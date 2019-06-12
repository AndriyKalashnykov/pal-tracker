#!/bin/bash
# set -xv

gradle clean build
cf push pal-tracker --random-route --no-start -p ./build/libs/pal-tracker.jar
cf restart pal-tracker
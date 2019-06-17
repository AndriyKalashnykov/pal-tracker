#!/bin/bash
# set -xv


gradle clean build

cf delete pal-tracker -r -f
cf push pal-tracker --random-route --no-start -p ./build/libs/pal-tracker.jar
cf restart pal-tracker

cf create-service p.mysql db-small tracker-database
cf service tracker-database

cf bind-service pal-tracker tracker-database

cf set-env pal-tracker MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE "*"
cf set-env pal-tracker MANAGEMENT_ENDPOINT_HEALTH_SHOWDETAILS always
cf restage pal-tracker
#!/bin/bash
# In most cases, development with webpack's dev server and separate service is more convenient,
# but this can be used to test the service with paths as deployed in the environments:
# (remember to install both ui and service modules)
java -Dspring.profiles.active=dev -Dfront.kayttooikeus-service.virkailija-ui.basePath=/kayttooikeus-service/virkailija -jar target/kayttooikeus-service-1.0.0-SNAPSHOT.jar --spring.config.location=${HOME}/oph-configuration/kayttooikeus.yml

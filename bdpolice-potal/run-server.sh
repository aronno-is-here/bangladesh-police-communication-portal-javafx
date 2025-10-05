#!/usr/bin/env bash
set -e
if ! command -v mvn >/dev/null; then echo "Please install Maven (mvn)."; exit 1; fi
mvn -q -f server/pom.xml spring-boot:run

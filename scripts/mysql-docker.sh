#!/usr/bin/env bash

docker run -p 3306:3306 --name spring-security-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bank -d mysql
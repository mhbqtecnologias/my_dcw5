#!/bin/bash

docker rm -f $(docker ps -qa) || true
docker rmi marcioholanda/app-dcw5:develop || true

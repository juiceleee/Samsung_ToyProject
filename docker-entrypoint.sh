#!/bin/bash

echo waiting
dockerize -wait tcp://docker-mysql:3306 -timeout 1m
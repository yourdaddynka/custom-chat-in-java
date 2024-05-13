#!/bin/bash

# Устанавливаем утилиту mongo
apt-get update
apt-get install -y mongodb-clients

# Запускаем MongoDB
exec docker-entrypoint.sh mongod

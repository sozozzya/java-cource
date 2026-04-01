#!/bin/bash

echo "Waiting for Kafka..."

cub kafka-ready -b kafka-1:9092 1 30

echo "Creating topic..."

kafka-topics \
  --bootstrap-server kafka-1:9092 \
  --create \
  --if-not-exists \
  --topic transfers-topic \
  --partitions 3 \
  --replication-factor 3

echo "Topic created"
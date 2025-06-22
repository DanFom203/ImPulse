#!/bin/bash

TAG="latest"

while getopts t: flag; do
  case "${flag}" in
    t) TAG=${OPTARG} ;;
    *) echo "Неверный параметр: -$OPTARG" >&2; exit 1 ;;
  esac
done

export TAG=$TAG

echo "Запуск контейнеров с тегом: $TAG"
docker-compose up -d


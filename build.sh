#!/bin/bash

TAG="latest"

while getopts t: flag; do
  case "${flag}" in
    t) TAG=${OPTARG} ;;
    *) echo "Неверный параметр: -$OPTARG" >&2; exit 1 ;;
  esac
done

echo "Сборка образов с тегом: $TAG"

docker build -t backend:"$TAG" ./backend
docker build -t frontend:"$TAG" ./frontend
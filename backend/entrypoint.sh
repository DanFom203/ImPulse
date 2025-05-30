#!/bin/sh

echo "🔄 Ожидание доступности PostgreSQL через haproxy..."

until nc -z haproxy 5432; do
  echo "⏳ Хост haproxy:5432 ещё недоступен. Жду..."
  sleep 2
done

echo "✅ PostgreSQL через haproxy доступен. Запускаю приложение..."
exec java -jar app.jar
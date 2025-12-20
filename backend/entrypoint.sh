#!/bin/sh

echo "🔄 Ожидание доступности PostgreSQL..."

until nc -z postgres 5432; do
  echo "⏳ Хост postgres:5432 ещё недоступен. Жду..."
  sleep 2
done

echo "✅ PostgreSQL доступен. Запускаю приложение..."
exec java -jar app.jar

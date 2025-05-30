FROM eclipse-temurin:18-jdk

VOLUME /tmp
WORKDIR /app

# Установка netcat
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# Копируем jar
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Копируем entrypoint-скрипт и делаем его исполняемым
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

# Используем его как точку входа
ENTRYPOINT ["./entrypoint.sh"]

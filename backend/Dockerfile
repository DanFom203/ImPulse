# Этап сборки
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY --chown=gradle:gradle . .

USER root
RUN rm -rf build
RUN chown -R gradle:gradle /app

USER gradle
RUN gradle clean build -x test --no-daemon --stacktrace --info --gradle-user-home /app/.gradle

# Этап запуска
FROM eclipse-temurin:21-jdk

VOLUME /tmp
WORKDIR /app

# Установка netcat
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Копируем jar
COPY --from=build /app/build/libs/*.jar app.jar

# Копируем entrypoint-скрипт и делаем его исполняемым
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

# Используем его как точку входа
ENTRYPOINT ["./entrypoint.sh"]

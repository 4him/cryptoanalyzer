# Указываем базовый образ
FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl
# Создаем том для временных файлов (опционально)
VOLUME /tmp

# Аргумент для указания пути к JAR-файлу
ARG JAR_FILE=target/crypto-analyzer-0.0.1-SNAPSHOT.jar

# Копируем скомпилированный JAR-файл в контейнер
COPY ${JAR_FILE} app.jar

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app.jar"]

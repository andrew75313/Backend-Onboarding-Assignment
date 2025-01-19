# Step 1: Gradle 빌드 환경으로 사용할 공식 Gradle 이미지를 기반으로 설정
FROM gradle:8.1.1-jdk17 AS build

# 빌드 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드에 필요한 파일들을 복사
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# 애플리케이션 빌드 (테스트 포함)
RUN ./gradlew clean build

# Step 2: 실행 환경으로 사용할 가벼운 JDK 이미지 설정
FROM eclipse-temurin:17-jdk-alpine

# 애플리케이션 실행 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 실행 환경으로 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션에서 사용할 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 실행될 명령어 정의
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Phase 01 — Setup Spring Boot Project

## Overview
Tạo Spring Boot 3.x project với Gradle, add dependency Netty-SocketIO.

## Files
- `demo-netty-socketio/settings.gradle`
- `demo-netty-socketio/build.gradle`
- `demo-netty-socketio/src/main/java/com/demo/netty/NettySocketIoDemoApplication.java`
- `demo-netty-socketio/src/main/resources/application.yml`

## Dependencies chính
- `org.springframework.boot:spring-boot-starter-web` — REST publish endpoint
- `com.corundumstudio.socketio:netty-socketio:2.0.12` — Netty-SocketIO server
- `org.projectlombok:lombok` — giảm boilerplate

## build.gradle
```gradle
plugins {
  id 'org.springframework.boot' version '3.2.5'
  id 'io.spring.dependency-management' version '1.1.4'
  id 'java'
}

group = 'com.demo.netty'
version = '0.0.1'
java.sourceCompatibility = JavaVersion.VERSION_17

repositories { mavenCentral() }

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-web'
  implementation 'com.corundumstudio.socketio:netty-socketio:2.0.12'
  compileOnly 'org.projectlombok:lombok'
  annotationProcessor 'org.projectlombok:lombok'
}
```

## application.yml
```yaml
server:
  port: 8080

socketio:
  host: 0.0.0.0
  port: 9092
  origin: "*"
  jwt-secret: demo-secret-key-change-me
```

## Steps
1. Tạo thư mục `demo-netty-socketio/`
2. Tạo `settings.gradle`, `build.gradle`
3. Tạo main class `NettySocketIoDemoApplication.java`
4. Tạo `application.yml`
5. `./gradlew build` → verify compile OK

## Todo
- [ ] Tạo project structure
- [ ] Verify Gradle sync
- [ ] Run `./gradlew bootRun` → Spring Boot start OK

## Success Criteria
- Gradle build success
- Spring Boot khởi động trên port 8080
- Chưa có Netty-SocketIO (sẽ thêm ở Phase 02)

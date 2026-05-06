# .gitignore 파일을 복사해서 .dockerignore 파일도 함께 작성

# docker build -t fisa06-sb:latest .
# docker run -p 8080:8080 --env-file=.env fisa06-sb:latest

# Stage 1: Build stage (멀티 스테이지 빌드를 사용해 불필요한 파일 제거)
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# ── 레이어 1: 의존성 선언 파일만 복사 ──────────────────────────
COPY pom.xml ./

# ── 레이어 2: 의존성 다운로드 (핵심 캐싱 레이어) ─────────────────
# pom.xml이 바뀌지 않으면 이 레이어는 캐시를 재사용합니다.
# → 소스 코드가 아무리 바뀌어도 수백 MB 라이브러리를 재다운로드하지 않습니다.
RUN mvn dependency:go-offline -B

# ── 레이어 3: 소스 코드 복사 (여기서부터 캐시 MISS 가능) ─────────
COPY src ./src

# ── 레이어 4: 패키징 ─────────────────────────
# 빌드 시 테스트를 생략하면 빌드 속도가 빨라집니다.
RUN mvn package -DskipTests

# Stage 2: Slim Run stage (최소한의 런타임 환경 사용)
FROM amazoncorretto:21-alpine
LABEL maintainer="fisaai"

# 비루트 유저 생성
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령
CMD ["java", "-jar", "app.jar"]




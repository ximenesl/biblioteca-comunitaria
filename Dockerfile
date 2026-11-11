# ==============================================================================
# Multi-stage Dockerfile para Biblioteca Comunitária
# Java 21 LTS - Alpine Linux - Non-root Security
# ==============================================================================

# ------------------------------------------------------------------------------
# Estágio 1: Build da aplicação com Maven
# ------------------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /workspace

# Otimização de cache para dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Cópia do código-fonte e compilação do pacote JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ------------------------------------------------------------------------------
# Estágio 2: Runtime seguro e enxuto com JRE 21
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runner

# Metadados da imagem
LABEL maintainer="Lucas Ximenes" \
      version="2.0.0" \
      description="API Biblioteca Comunitaria - Spring Boot 3 & Arquitetura Hexagonal"

# Instalação do curl para verificação de integridade (Healthcheck / QA)
RUN apk add --no-cache curl tzdata

# Criação de usuário e grupo de sistema não-privilegiados (princípio do menor privilégio)
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Cópia do JAR gerado no estágio de build com permissão exclusiva ao usuário spring
COPY --from=builder --chown=spring:spring /workspace/target/*.jar /app/app.jar

# Variáveis padrão de ambiente da JVM para containers
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Alterna para usuário não-root
USER spring:spring

EXPOSE 8080

# Verificação contínua de saúde do contêiner (Docker QA / Readiness)
HEALTHCHECK --interval=15s --timeout=5s --start-period=25s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/livros || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]


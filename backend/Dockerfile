FROM maven:3.9.5-eclipse-temurin-11-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B --no-transfer-progress && \
    rm -rf ~/.m2/repository/.cache ~/.m2/repository/org/apache/maven

COPY src ./src

ARG SPRING_PROFILES_ACTIVE=prod
ARG JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat"

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV JAVA_OPTS=${JAVA_OPTS}

RUN mvn clean package -DskipTests -B --no-transfer-progress && \
    mv target/*.jar app.jar && \
    rm -rf target ~/.m2/repository /tmp/* /var/tmp/*

FROM eclipse-temurin:11-jre-alpine

RUN apk add --no-cache curl dumb-init && \
    rm -rf /var/cache/apk/* /tmp/* /var/tmp/* /root/.cache /root/.npm

WORKDIR /app

RUN addgroup -S appgroup && \
    adduser -S -D -H -u 1000 -s /sbin/nologin -G appgroup -g appuser appuser

COPY --from=builder /app/app.jar app.jar

RUN chown -R appuser:appgroup /app && \
    chmod 400 app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/usr/bin/dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
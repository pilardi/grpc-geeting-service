FROM amazoncorretto:8-alpine-jdk

LABEL org.label-schema.description "grpc geeting service"
LABEL org.label-schema.schema-version "1.0"

ARG JAR=grpc-geeting-service-server-jar-with-dependencies.jar
ADD target/${JAR} /usr/share/service/service.jar

# https://github.com/grpc-ecosystem/grpc-health-probe/
RUN GRPC_HEALTH_PROBE_VERSION=v0.4.5 && \
    wget -qO/bin/grpc_health_probe https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/${GRPC_HEALTH_PROBE_VERSION}/grpc_health_probe-linux-amd64 && \
    chmod +x /bin/grpc_health_probe

# https://security.stackexchange.com/questions/106860/can-a-root-user-inside-a-docker-lxc-break-the-security-of-the-whole-system
ARG USER=spring
ARG GROUP=spring
RUN addgroup -S ${GROUP} && adduser -S ${USER} -G ${GROUP}
USER ${USER}:${GROUP}

ENV SPRING_PROFILES_ACTIVE ""
# JVM settings
ENV JAVA_SETTINGS "-noverify -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dspring.jmx.enabled=false"
# MEM settings + others
ENV JAVA_OPTS ""
# SPRING BOOT SETTINGS (ie port) (https://spring.io/guides/topicals/spring-boot-docker)
ENV JAVA_TOOL_OPTIONS ""
ENV LOGGING_LEVEL ""

ENTRYPOINT [ "/bin/sh", "-c", "exec java ${JAVA_SETTINGS} -jar /usr/share/service/service.jar --logging.level.root=${LOGGING_LEVEL:-info} ${@}" ]

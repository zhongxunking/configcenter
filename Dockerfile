FROM store/oracle/serverjre:8
COPY /configcenter-assemble/target/configcenter-exec.jar /apps/configcenter/configcenter-exec.jar
VOLUME  /var/apps
EXPOSE 6220
ENV JAVA_OPTS=""
ENTRYPOINT java $JAVA_OPTS -jar /apps/configcenter/configcenter-exec.jar

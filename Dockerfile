FROM tomcat:10.1-jre17-temurin

COPY target/weather-viewer.war /usr/local/tomcat/webapps/weather-viewer.war
COPY postgresql-42.7.3.jar /usr/local/tomcat/lib/

EXPOSE 8080
ENV CATALINA_OPTS="-Xmx512m"

CMD ["catalina.sh", "run"]

FROM tomcat:10.1-jdk21-openjdk

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to Tomcat webapps directory
COPY target/java-blog-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Create a directory for application properties
RUN mkdir -p /usr/local/tomcat/conf/blog

# Copy properties file (you can also use environment variables)
COPY src/main/resources/application.properties /usr/local/tomcat/conf/blog/

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
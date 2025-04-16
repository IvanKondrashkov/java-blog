FROM tomcat:10.1-jdk21-openjdk

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to Tomcat webapps directory
COPY target/java-blog-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Create a directory for application properties
RUN mkdir -p /usr/local/tomcat/conf/blog

# Copy properties file (you can also use environment variables)
COPY src/main/resources/application.properties /usr/local/tomcat/conf/blog/

# Set environment variables (can be overridden in docker-compose)
ENV AWS_REGION=us-east-1
ENV AWS_SERVICE_ENDPOINT=your-service-endpoint
ENV AWS_S3_BUCKET_NAME=your-bucket-name
ENV AWS_ACCESS_KEY=your-access-key
ENV AWS_SECRET_KEY=your-secret-key

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
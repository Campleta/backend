# JETTY
 FROM jetty
 EXPOSE 8080
 COPY ./target/campleta-1.0.0.war /var/lib/jetty/webapps/ROOT.war
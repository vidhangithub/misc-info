import org.apache.catalina.Context;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatJndiConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void customizeContext(Context context, org.springframework.boot.web.server.WebServerFactoryCustomizer customizer) {
                ContextResource resource = new ContextResource();
                resource.setName("jdbc/mydb2");
                resource.setType("javax.sql.DataSource");
                resource.setProperty("driverClassName", "com.ibm.db2.jcc.DB2Driver");
                resource.setProperty("url", "jdbc:db2://your-db2-server:50000/yourDBName:sslConnection=true;");
                resource.setProperty("username", "yourUsername");
                resource.setProperty("password", "yourPassword");

                // Connection Pool Settings
                resource.setProperty("initialSize", "5");
                resource.setProperty("maxActive", "50");
                resource.setProperty("maxIdle", "20");
                resource.setProperty("minIdle", "5");
                resource.setProperty("maxWait", "10000");
                resource.setProperty("removeAbandonedTimeout", "60");
                resource.setProperty("timeBetweenEvictionRunsMillis", "5000");
                resource.setProperty("minEvictableIdleTimeMillis", "30000");
                resource.setProperty("validationQuery", "SELECT 1 FROM SYSIBM.SYSDUMMY1");
                resource.setProperty("testOnBorrow", "true");
                resource.setProperty("testWhileIdle", "true");
                resource.setProperty("testOnReturn", "false");

                // SSL Configuration
                System.setProperty("javax.net.ssl.keyStore", "/path/to/keystore.jks");
                System.setProperty("javax.net.ssl.keyStorePassword", "yourKeystorePassword");
                System.setProperty("javax.net.ssl.trustStore", "/path/to/truststore.jks");
                System.setProperty("javax.net.ssl.trustStorePassword", "yourTruststorePassword");

                context.getNamingResources().addResource(resource);
            }
        };
    }
}

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

@Configuration
public class TomcatJndiConfigSecond {

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(org.apache.catalina.Context context) {
                try {
                    // Set up the JNDI context and resource
                    Context envContext = (Context) new InitialContext().lookup("java:/comp/env");

                    // Define the connection pool properties
                    PoolProperties poolProps = new PoolProperties();
                    poolProps.setUrl("jdbc:db2://your-db2-server:50000/yourDBName:sslConnection=true;");
                    poolProps.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
                    poolProps.setUsername("yourUsername");
                    poolProps.setPassword("yourPassword");

                    // SSL Configuration
                    System.setProperty("javax.net.ssl.keyStore", "/path/to/keystore.jks");
                    System.setProperty("javax.net.ssl.keyStorePassword", "yourKeystorePassword");
                    System.setProperty("javax.net.ssl.trustStore", "/path/to/truststore.jks");
                    System.setProperty("javax.net.ssl.trustStorePassword", "yourTruststorePassword");

                    // Additional connection pool settings
                    poolProps.setInitialSize(5);          // Minimum number of idle connections in the pool
                    poolProps.setMaxActive(50);           // Maximum number of active connections in the pool
                    poolProps.setMaxIdle(20);             // Maximum number of idle connections in the pool
                    poolProps.setMinIdle(5);              // Minimum number of idle connections in the pool
                    poolProps.setMaxWait(10000);          // Maximum time to wait for a connection from the pool
                    poolProps.setRemoveAbandonedTimeout(60); // Timeout before a connection is considered abandoned
                    poolProps.setTimeBetweenEvictionRunsMillis(5000); // Time between runs of the idle connection evictor thread
                    poolProps.setMinEvictableIdleTimeMillis(30000);   // Minimum amount of time a connection can remain idle before being evicted
                    poolProps.setValidationQuery("SELECT 1 FROM SYSIBM.SYSDUMMY1"); // Validation query to check the health of the connection
                    poolProps.setTestOnBorrow(true);      // Test the connection before borrowing from the pool
                    poolProps.setTestWhileIdle(true);     // Test the connection while it is idle
                    poolProps.setTestOnReturn(false);     // Test the connection before returning it to the pool

                    // Set the default schema
                    poolProps.setConnectionProperties("currentSchema=YOURSCHEMANAME");

                    // Create the DataSource and bind it to the JNDI context
                    DataSource dataSource = new DataSource(poolProps);
                    Reference ref = new Reference("javax.sql.DataSource", "org.apache.tomcat.jdbc.pool.DataSourceFactory", null);
                    ref.add(new StringRefAddr("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory"));
                    envContext.bind("jdbc/MyDB2DataSource", dataSource);
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

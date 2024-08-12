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
public class TomcatJndiConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(org.apache.catalina.Context context) {
                try {
                    // Set up the JNDI context and resource
                    Context envContext = (Context) new InitialContext().lookup("java:/comp/env");

                    // Define your DataSource properties
                    PoolProperties p = new PoolProperties();
                    p.setUrl("jdbc:mysql://localhost:3306/mydb");
                    p.setDriverClassName("com.mysql.cj.jdbc.Driver");
                    p.setUsername("root");
                    p.setPassword("password");
                    p.setMaxActive(20);
                    p.setMaxIdle(10);
                    p.setMaxWait(10000);

                    DataSource dataSource = new DataSource(p);

                    // Create a reference for the DataSource and bind it to the JNDI context
                    Reference ref = new Reference("javax.sql.DataSource", "org.apache.tomcat.jdbc.pool.DataSourceFactory", null);
                    ref.add(new StringRefAddr("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory"));
                    envContext.bind("jdbc/MyDataSource", dataSource);
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

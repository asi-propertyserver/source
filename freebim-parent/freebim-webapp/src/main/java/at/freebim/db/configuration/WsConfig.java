package at.freebim.db.configuration;

import com.sun.xml.ws.transport.http.servlet.WSSpringServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = "classpath:jaxwsconfig.xml")
@ComponentScan(basePackages = {"at.freebim.db.webservice"})
public class WsConfig {
  @Bean
  public ServletRegistrationBean<WSSpringServlet> servletRegistrationBean() {
    return new ServletRegistrationBean<WSSpringServlet>(new WSSpringServlet(),
                                                        "/FreebimWebservice");
  }
}

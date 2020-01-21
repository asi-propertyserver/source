package at.freebim.db.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.freebim.db.webapp.security.FreebimBasicAuthenticationEntryPoint;
import at.freebim.db.webapp.security.jwt.JwtTokenFilter;
import at.freebim.db.webapp.security.jwt.JwtTokenProvider;

@Configuration
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class FreebimConfiguration extends WebSecurityConfigurerAdapter {

	static final Logger logger = LoggerFactory.getLogger(FreebimConfiguration.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private FreebimBasicAuthenticationEntryPoint freebimBasicAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disable CSRF (cross site request forgery)
		http.csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider, authenticationManager()),
				UsernamePasswordAuthenticationFilter.class);

		// Entry points
		http.authorizeRequests().antMatchers("/").permitAll()

				// This sites are only for swagger
				.antMatchers("/v2/api-docs").permitAll().antMatchers("/api-docs").permitAll()
				.antMatchers("/swagger-resources/**").permitAll().antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/configuration/**").permitAll().antMatchers("/webjars/**").permitAll()
				.antMatchers("/public").permitAll().antMatchers("/resources/**").permitAll()
				// Allow any other route

				.antMatchers("/login/rest").permitAll().antMatchers("/login/refresh/token").permitAll()
				
				//impressum
				.antMatchers("/company/graph").permitAll().antMatchers("/company/logo/**").permitAll()
				
				.antMatchers("/FreebimWebservice").permitAll().and().anonymous().and().authorizeRequests().anyRequest()
				.authenticated();

		// If a user try to access a resource without having enough permissions
		http.exceptionHandling().accessDeniedPage("/");

		// Optional, if you want to test the API from a browser
		http.httpBasic().authenticationEntryPoint(freebimBasicAuthenticationEntryPoint);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}

package at.freebim.db.configuration;

import java.io.File;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.neo4j.driver.v1.Config;
import org.neo4j.ogm.config.AutoIndexMode;
import org.neo4j.ogm.config.ClasspathConfigurationSource;
import org.neo4j.ogm.config.ConfigurationSource;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories("at.freebim.db.repository")
@EntityScan(basePackages = "at.freebim.db.domain")
@ComponentScan(basePackages = { "at.freebim.db.service", "net.spectroom.neo4j" })
@PropertySources({ @PropertySource("file:/etc/asi-propertyserver/production/app.properties") })
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class Neo4jTestConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(Neo4jTestConfiguration.class);
	
	@Value("${db.uri}")
	private String uri;

	@Value("${db.username}")
	private String username;

	@Value("${db.password}")
	private String password;

	@Bean
	public org.neo4j.ogm.config.Configuration configuration() {
		org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder()
				.autoIndex(AutoIndexMode.NONE.toString())
				.encryptionLevel(Config.EncryptionLevel.REQUIRED.toString()).uri(this.uri)
				.credentials(this.username, this.password).build();

		return configuration;

	}

	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory(configuration(), "at.freebim.db.domain", "at.freebim.db.repository",
				"at.freebim.db.service");
	}

	@Bean
	public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(sessionFactory());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		return executor;
	}

	@Bean
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		
		boolean customConfig = true;
		FileSystemResource fr = new FileSystemResource("/etc/asi-propertyserver/production/app.properties");
		File frf = fr.getFile();
		if (!frf.exists()) {
			logger.info("No user specific properties file at: [{}].", frf.getAbsolutePath());
			customConfig = false;
		}
		if (customConfig && !frf.canRead()) {
			logger.info("User specific properties file [{}] not readable.", frf.getAbsolutePath());
			customConfig = false;
		}
		if (customConfig) {
			logger.info("Using user specific properties file [{}].", frf.getAbsolutePath());
		}
		
		Resource[] resources = new Resource[] {
				fr
		};
		
		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		pspc.setIgnoreResourceNotFound(true);
		pspc.setLocalOverride(true);

		logger.info("{} instantiated.", pspc.getClass().getName());
		return pspc;
	}

}

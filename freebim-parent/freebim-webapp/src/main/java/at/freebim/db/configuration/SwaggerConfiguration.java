package at.freebim.db.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// TODO: comes with next release
//@Configuration
//@EnableSwagger2
//@Profile("release")
public class SwaggerConfiguration {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(apiPaths())
				.build().apiInfo(apiInfo()).securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiInfo apiInfo() { // TODO: adapt
		return new ApiInfo("asi-propertyserver", "Some custom description of API.", "API 1.0", "Terms of service",
				new Contact("John Doe", "www.example.com", "myeaddress@company.com"), "License of API",
				"API license URL", Collections.emptyList());
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(securityPaths()).build();
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> securityPaths() {
		return Predicates.and(

				PathSelectors.regex("/.*"), Predicates.not(PathSelectors.regex("/login/refresh/token.*")),
				Predicates.not(PathSelectors.regex("/login/rest.*")));

	}

	@SuppressWarnings("unchecked")
	private Predicate<String> apiPaths() {
		return Predicates.and(

				PathSelectors.regex("/.*"), Predicates.not(PathSelectors.regex("/")),
				Predicates.not(PathSelectors.regex("/api-docs.*")),
				Predicates.not(PathSelectors.regex("/auth-error.*")),
				Predicates.not(PathSelectors.regex("/login-error.*")), Predicates.not(PathSelectors.regex("/error.*")));

	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;

		ArrayList<SecurityReference> l = new ArrayList<SecurityReference>();
		l.add(new SecurityReference("JWT", authorizationScopes));
		return l;
	}
}

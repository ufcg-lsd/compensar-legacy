package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springboot.controller.EmailController;
import springboot.controller.QuestaoController;
import springboot.controller.UsuarioController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackageClasses = { UsuarioController.class,
		EmailController.class, QuestaoController.class })
@Configuration
public class SwaggerConfig {

	private static final String SWAGGER_API_VERSION = "1.0";
	private static final String LICENSE_TEXT = "License";
	private static final String title = "Ambiente de Estudo sobre o Pensamento Computacional REST API";
	private static final String description = "RESTful API para ComPensar";

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(title).description(description).license(LICENSE_TEXT)
				.version(SWAGGER_API_VERSION).build();
	}

	@Bean
	public Docket productsApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/").select()
				.paths(PathSelectors.regex("/api.*")).build();
	}

}

package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração CORS para a aplicação Spring Boot. Esta classe define as regras
 * de compartilhamento de recursos entre origens diferentes (Cross-Origin
 * Resource Sharing) para permitir que o front-end e back-end comuniquem-se de
 * forma segura quando hospedados em domínios distintos.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * Configura as regras de CORS para toda a aplicação.
	 * 
	 * @param registry O registro de configurações CORS.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
				.allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept")
				.exposedHeaders("Location")
				.maxAge(3600);
	}
}

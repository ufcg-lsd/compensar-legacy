package springboot;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import springboot.config.TokenFilter;

@SpringBootApplication
public class AepcApplication {

	private static final List<String> PROTECTED_URL_PATTERNS = Arrays.asList(
			"/api/auth/authenticate/*",
			"/api/listaquestoes/*",
			"/api/questao/*",
			"/api/competencias/*",
			"/api/usuario/*",
			"/api/avaliacao/*",
			"/api/conteudo/*",
			"/api/updateClassificador",
			"/api/cursoAvaliacao/*",
			"/api/cursoCriacao/*",
			"/api/cursos/*");

	/**
	 * Registra o filtro JWT para proteger rotas específicas.
	 *
	 * @return Configuração do filtro de autenticação JWT.
	 */
	@Bean
	public FilterRegistrationBean<TokenFilter> filterJwt() {
		FilterRegistrationBean<TokenFilter> filterRb = new FilterRegistrationBean<>();
		filterRb.setFilter(new TokenFilter());
		filterRb.setUrlPatterns(PROTECTED_URL_PATTERNS);
		return filterRb;
	}

	/**
	 * Configura o filtro de CORS para permitir acessos de diferentes origens.
	 *
	 * @return Configuração do filtro CORS.
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		SpringApplication.run(AepcApplication.class, args);
	}
}

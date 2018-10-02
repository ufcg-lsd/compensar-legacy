package springboot.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import springboot.security.AepcUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AepcUserDetailsService aepcUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests().antMatchers("/api/**").hasRole("ALL")
				.antMatchers("/api/alternativa/**", "/api/questaoObj/**", "/api/questaoSubj/**")
				.hasAnyRole("CRIA_QUESTOES").antMatchers("/api/usuario/**").hasAnyRole("CRIA_QUESTOES","RESPONDE_QUESTOES")
				.antMatchers(HttpMethod.GET, "/api/alternativa/**", "/api/questaoObj/**", "/api/questaoSubj/**")
				.hasRole("READ_ONLY")
				.anyRequest().authenticated().and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(aepcUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}

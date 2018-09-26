package springboot.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment env;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	private final String[] PUBLIC_MATCHERS_GET = {
			"/h2-console/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{

		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();			
		}
		http.csrf().disable();
		

		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.anyRequest().authenticated()
			.and().httpBasic();
	}
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		String username = "aepc";
		String password = "123";
		
		String passwordEncoded = this.bCryptPasswordEncoder().encode(password);
		auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
			.withUser(username).password(passwordEncoded).roles("USER", "ADMIN");
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

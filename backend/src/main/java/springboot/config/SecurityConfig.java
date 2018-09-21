package springboot.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	

	
	/**
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 String password = passwordEncoder().encode("123");
		 auth.inMemoryAuthentication().withUser("marcelo").password(password).roles("ADMIN");
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	*/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
    
    
	  @Override
	  public void configure(AuthenticationManagerBuilder builder) throws Exception {
		 String password = passwordEncoder().encode("123");
	    builder
	        .inMemoryAuthentication()
	        .withUser("marcelo").password(password)
	            .roles("ADMIN");
	  }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/api/usuario")
            .hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin()
            .permitAll().and().logout().permitAll();

        http.csrf().disable();
    }
	
	/**
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
		  http
		    .authorizeRequests()
		        //.antMatchers("/resources/**", "/webjars/**").permitAll()
		        .anyRequest().authenticated()
		    .and()
		    .httpBasic();
	  }
			
			

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			 String password = passwordEncoder().encode("123");
			auth.inMemoryAuthentication().withUser("zico").password(password).roles("ADMIN");
		}
	
		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
		
		*/
}

package br.com.icaro.agende.secutrity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.icaro.agendesecutiry.jwt.JWTAuthenticationFilter;
import br.com.icaro.agendesecutiry.jwt.JWTLoginFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticatorProvider customAuthenticatorProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().httpBasic()
			.and().authorizeRequests().antMatchers(HttpMethod.POST, "/api/app/login").permitAll()
			.and().authorizeRequests().antMatchers(HttpMethod.POST, "/api/app/token/refresh").permitAll()
			.and().authorizeRequests().antMatchers("/api/app/**").authenticated()
			.and().addFilterBefore(new JWTLoginFilter("/api/app/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.csrf().disable();
	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticatorProvider);
	}

}

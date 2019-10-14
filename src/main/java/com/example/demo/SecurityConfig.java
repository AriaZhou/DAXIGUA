package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	 protected void configure(HttpSecurity http) throws Exception {
	  http
	   .authorizeRequests()
	    .antMatchers("/css/**", "/picture/**").permitAll()
	    .antMatchers("/user/**","/index").hasRole("USER")
	    .antMatchers("/admin/**").hasRole("ADMIN")
	    .and()
	   	.formLogin()
	    .loginPage("/login").failureUrl("/login-error")
		.defaultSuccessUrl("/").successHandler(new LoginSuccessHandle())
	  	.permitAll().and().rememberMe();
	  
	  http.csrf().disable();
	 
	 }

	  @Autowired
	 private DataSource dataSource;
	 
	 @Autowired
	 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  auth
	  .jdbcAuthentication()
	   .dataSource(dataSource)
	   .usersByUsernameQuery(
	    "select username,password, enabled from user where username=?")
	   .authoritiesByUsernameQuery(
	    "select username, role from user where username=?");
	 }

	/*
	 * 注入BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


}

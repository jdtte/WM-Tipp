package edu.hm.cs.swt2ss18.wmtipp;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Boot Configuration. 
 * 
 * @author katz.bastian
 */
@Configuration
public class WmtippConfiguration extends WebSecurityConfigurerAdapter {

  @Override
 protected void configure(HttpSecurity http) throws Exception {
  http.authorizeRequests()	
         .antMatchers("/resources/**").permitAll() //für css und login img
         .antMatchers("/login**", "/logout**").permitAll()
         .antMatchers("/admin", "/h2_console/**").hasRole("ADMIN")
         .anyRequest().fullyAuthenticated()
         .and()
  .formLogin()
      .loginPage("/login").permitAll()
        .and()
  .logout().permitAll();
    http.csrf().disable();
    http.headers().frameOptions().disable();
  }

}

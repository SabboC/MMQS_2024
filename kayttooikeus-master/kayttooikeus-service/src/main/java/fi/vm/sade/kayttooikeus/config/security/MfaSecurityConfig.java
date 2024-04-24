package fi.vm.sade.kayttooikeus.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import fi.vm.sade.kayttooikeus.config.properties.CasProperties;

@Configuration
@Order(1)
public class MfaSecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String ROLE = "APP_KAYTTOOIKEUS_PALVELUKAYTTAJA_READ";

  private CasProperties casProperties;

  @Autowired
  public MfaSecurityConfig(CasProperties casProperties) {
      this.casProperties = casProperties;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .requestMatchers()
            .antMatchers("/mfa/**")
        .and()
            .csrf().disable()
            .headers().disable()
            .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .httpBasic()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
              .withUser(casProperties.getMfa().getUsername())
              .password("{noop}" + casProperties.getMfa().getPassword())
              .roles(ROLE);
  }
}

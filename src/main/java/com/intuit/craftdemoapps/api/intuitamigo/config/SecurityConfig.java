package com.intuit.craftdemoapps.api.intuitamigo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.intuit.craftdemoapps.api.intuitamigo.service.ProfileService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableRedisHttpSession
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private ProfileService profileService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService)profileService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec.csrf()
        	.disable()
        	.logout()
        	.clearAuthentication(true)
        	.logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/profiles/logout"))
        	.and()
        	.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/profiles/login").permitAll()
            .antMatchers("/api/v1/profiles/signup").permitAll()
            .antMatchers("/api/api-docs**").permitAll()
            .antMatchers("/api/swagger**").permitAll()
            .anyRequest()
            .authenticated();
    }
    
}

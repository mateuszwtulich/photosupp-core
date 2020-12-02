package com.wtulich.photosupp.general.security.config;

import com.wtulich.photosupp.general.security.authentication.logic.impl.jwt.filter.JwtUserAuthenticationFilter;
import com.wtulich.photosupp.general.security.authentication.logic.impl.jwt.verifier.JwtVerifier;
import com.wtulich.photosupp.general.security.authentication.logic.impl.usecase.UcLoginImpl;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UcLoginImpl ucLogin;

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, UcLoginImpl ucLogin) {
        this.passwordEncoder = passwordEncoder;
        this.ucLogin = ucLogin;
    }

    @Override protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUserAuthenticationFilter(authenticationManager(), ucLogin))
                .addFilterAfter(new JwtVerifier(), JwtUserAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/user/v1/user").permitAll()
                .antMatchers("/user/v1/user/account").permitAll()
                .antMatchers("/user/v1/user/account/registrationConfirm**").permitAll()
                .antMatchers("/service/v1/service/calculate").permitAll()
                .antMatchers("/service/v1/services").permitAll()
                .antMatchers("/service/v1/indicators").permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui.html").permitAll()
                .antMatchers("/**").hasAnyAuthority(ApplicationPermissions.AUTH_USER.name())
                .anyRequest()
                .authenticated();

        http.cors();
    }

    @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(ucLogin);
        return provider;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

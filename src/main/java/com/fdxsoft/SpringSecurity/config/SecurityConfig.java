package com.fdxsoft.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .successHandler(this.successHandler()) // Si las credenciales son correctas, se ejecuta el
                                                               // successHandler
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/error")
                        .sessionFixation(fixation -> fixation.migrateSession())
                        .maximumSessions(1)
                        .sessionRegistry(this.sessionRegistry())
                        .expiredUrl("/login"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/v1/session-details"); // URL a la que se dirigira despues de iniciar sesion exitosamente
        };
    }

}

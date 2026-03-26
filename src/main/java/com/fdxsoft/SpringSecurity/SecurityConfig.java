package com.fdxsoft.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .build();
    }

    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/v1/index");
        };
    }

}

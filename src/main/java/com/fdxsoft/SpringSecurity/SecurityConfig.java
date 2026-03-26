package com.fdxsoft.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(Customizer.withDefaults()) // Asi se configura si vamos a trabajar con formularios / Si no, se usa
                                                 // esto: csrf -> csrf.disable()
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/v1/index2").permitAll().anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .build();
    }

}

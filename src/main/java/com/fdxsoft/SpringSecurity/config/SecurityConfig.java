package com.fdxsoft.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(Customizer.withDefaults()) // Se usa csrf -> csrf.disable() solo si no vamos a usar forms en el
                                                 // front-end. Pero lo ocupamos en el Postman para poder ejecutar algun
                                                 // metodo.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/v1/index2").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/createUser").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .successHandler(this.successHandler()) // Si las credenciales son correctas, se ejecuta el
                                                               // successHandler
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Se usa
                                                                                  // SessionCreationPolicy.STATELESS
                                                                                  // solo si NO vamos a usar forms en el
                                                                                  // front-end
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
            response.sendRedirect("/v1/session-details"); // URL a la que se dirigira despues de iniciar sesion
                                                          // exitosamente
        };
    }

    // *************************************************************************************************************
    // Cuando incormporamos estos tres metodos, estamos utilizando una configuracion
    // basica de spring security
    // pero usando un Usuario en memoria.
    // Por lo cual tenemos que eliminar los parametros que tenemos configurados en
    // application.properties
    // para que funcione, y ademas para que nos siga funcionando el formulario
    // tenemos que usar SessionCreationPolicy.IF_REQUIRED
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(User.withUsername("federico").password("admin").roles("ADMIN").build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Regresamos una instancia de NoOpPasswordEncoder, por que de momento, no se
        // requiere encriptar el password
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Ya no se necesita especificar el passwordEncoder ni el userDetailsService
        // en el AuthenticationManagerBuilder, el solito lo detecta de manera automatica
        return configuration.getAuthenticationManager();
    }
    // *************************************************************************************************************

}

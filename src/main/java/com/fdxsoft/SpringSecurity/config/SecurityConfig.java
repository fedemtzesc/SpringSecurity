package com.fdxsoft.SpringSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fdxsoft.SpringSecurity.filters.JwtAuthenticationFilter;
import com.fdxsoft.SpringSecurity.filters.JwtAuthorizationFilter;
import com.fdxsoft.SpringSecurity.utils.JwtUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)
            throws Exception {

        // Configuración del filtro JWT
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");

        return httpSecurity
                // 1. Deshabilitar CSRF (No se usa en APIs Stateless con JWT)
                .csrf(csrf -> csrf.disable())

                // 2. Configurar como STATELESS (El servidor no guarda sesiones)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Reglas de acceso
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/v1/index2").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/createUser").permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/accessAdmin").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/accessUser").hasRole("USER");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/accessDeveloper").hasRole("DEVELOPER");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/accessGuest").hasRole("GUEST");

                    auth.anyRequest().authenticated();
                })

                // 4. Agregar el filtro JWT
                .addFilter(jwtAuthenticationFilter)

                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)

                // NOTA: Se eliminó formLogin() para que no regrese HTML
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
    // El UserDetailsServiceImpl se encarga de cargar el usuario desde la base de
    // datos, por lo cual no es necesario configurar el userDetailsService en el
    // AuthenticationManagerBuilder, el solito lo detecta de manera automatica

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Regresamos una instancia de NoOpPasswordEncoder, por que de momento, no se
        // requiere encriptar el password
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Ya no se necesita especificar el passwordEncoder ni el userDetailsService
        // en el AuthenticationManagerBuilder, el solito lo detecta de manera automatica
        return configuration.getAuthenticationManager();
    }
    // *************************************************************************************************************

}

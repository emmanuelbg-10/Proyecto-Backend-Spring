package com.emmanuel.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesión, usaremos JWT
                .authorizeHttpRequests(authorize -> authorize
                        // Restringe POST y PUT para /api/v1/usuarios/*/libros
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/*/libros").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/*/libros").authenticated()

                        // Restringe PUT para /api/v1/usuarios/*
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/*").authenticated()

                        // Restringe POST y PUT para /api/v1/usuarios/*/libros/*/resenas
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/*/libros/*/resenas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/*/libros/*/resenas").authenticated()

                        // Restringe POST para /api/v1/usuarios/*/prestamos/*
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/*/prestamos/*").authenticated()

                        // Permite todas las demás solicitudes
                        .anyRequest().permitAll()
                )
                .cors(withDefaults()) // Habilita CORS con la configuración por defecto
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Añade el filtro JWT
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permite todos los orígenes
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Permite todos los métodos
        configuration.setAllowedHeaders(List.of("*")); // Permite todos los encabezados
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
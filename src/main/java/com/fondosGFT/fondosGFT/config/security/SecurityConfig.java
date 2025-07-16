package com.fondosGFT.fondosGFT.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // ESTO ES FUNDAMENTAL: Permite el acceso a TODO sin autenticación
                )
                .csrf(csrf -> csrf.disable()); // Deshabilita CSRF (importante para APIs y para evitar problemas en desarrollo)
        return http.build();
    }
}

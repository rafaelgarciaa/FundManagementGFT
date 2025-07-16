package com.fondosGFT.fondosGFT.config.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration class for the Spring Boot application.
 * It defines the access rules for HTTP resources, session management,
 * and the password encoder configuration.
 *
 * {@code @Configuration} indicates that this class contains Spring bean definitions.
 * {@code @EnableWebSecurity} enables Spring Security's web security features.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /**
     * Configures the HTTP security filter chain for the application.
     * It defines authorization rules for different paths, disables CSRF protection,
     * and sets the session management policy to "STATELESS", which is ideal for
     * RESTful APIs using token-based authentication (e.g., JWT).
     *
     * @param http The HttpSecurity object provided by Spring Security,
     * used to build the HTTP security rules.
     * @return An instance of {@link SecurityFilterChain} that defines how
     * security filters are applied to HTTP requests.
     * @throws Exception If an error occurs during the security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .requestMatchers("/login", "/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Provides a bean for the password encoder.
     * It uses {@link BCryptPasswordEncoder}, which is a strong and recommended
     * password hashing algorithm for securely storing passwords in the database.
     *
     * @return An instance of {@link PasswordEncoder} that can be used to
     * encode and verify passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
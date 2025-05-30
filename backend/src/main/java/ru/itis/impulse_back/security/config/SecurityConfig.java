package ru.itis.impulse_back.security.config;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.itis.impulse_back.security.filter.JWTAuthFilter;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Resource
    private Environment environment;

    @Value("${api.uri}")
    private String apiUri;

    private final JWTAuthFilter jwtAuthFilter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Initializing security configuration...");
        log.info("Permitting unauthenticated access to: {}/{}, /ws/**, /actuator/prometheus", apiUri, "auth/**");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.info("Session management: STATELESS");
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(apiUri + "/auth/**", "/ws/**", "/actuator/prometheus").permitAll();
                    auth.anyRequest().authenticated();
                    log.info("All other requests require authentication");
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED));
                    log.info("Exception handling configured: UNAUTHORIZED on auth failure");
                })
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        String[] origins = this.getAllowedOrigins();
                        configuration.setAllowedOrigins(List.of(origins));
                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        configuration.setAllowedHeaders(List.of("*"));
                        return configuration;
                    });
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        log.info("AuthenticationManager bean created using DAO authentication provider");
        return new ProviderManager(authenticationProvider);
    }

    private String[] getAllowedOrigins() {
        return environment.getProperty("api.cors.allowed-origins").split(",");
    }
}

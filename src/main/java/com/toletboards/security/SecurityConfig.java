package com.toletboards.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

               .authorizeHttpRequests(auth -> auth

    .requestMatchers("/api/auth/**").permitAll()

        .requestMatchers("/uploads/**").permitAll()

         .requestMatchers("/api/admin/**")
        .hasRole("ADMIN")


    // Public property APIs
    .requestMatchers(HttpMethod.GET, "/api/properties/**").permitAll()

    .requestMatchers("/api/admin/**").hasRole("ADMIN")

    .requestMatchers("/api/agent/**").hasAnyRole("AGENT", "ADMIN")

    .requestMatchers("/api/user/**").hasAnyRole("USER", "AGENT", "ADMIN")

    .requestMatchers("/api/visits/**")
.hasAnyRole("USER","AGENT","ADMIN")

    .anyRequest().authenticated()
)

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
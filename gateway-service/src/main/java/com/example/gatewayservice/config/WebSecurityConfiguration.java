package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/").permitAll()  // Разрешаем доступ без аутентификации
                        .pathMatchers("/crypto/stats/**").permitAll()  // Разрешаем доступ без аутентификации
                        .pathMatchers("/data/upload").permitAll()
                        .anyExchange().authenticated()
                );

        return http.build();
    }
}

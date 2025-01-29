package com.assignment.cryptoanalyzer.config.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() // Разрешаем доступ к корневому пути
                        .requestMatchers(HttpMethod.GET, "/crypto/stats/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/data/upload").permitAll()
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                );

        return http.build();
    }
}

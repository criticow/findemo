package com.waddahex.financeiroapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();

    // Permitir qualquer origem (CORS liberado)
    corsConfig.setAllowedOriginPatterns(List.of("*"));

    // Permitir todos os métodos HTTP
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // Permitir todos os headers
    corsConfig.setAllowedHeaders(List.of("*"));

    // Permitir que o frontend acesse determinados headers da resposta
    corsConfig.setExposedHeaders(List.of("Authorization", "token"));

    // Permitir envio de cookies e headers de autenticação
    corsConfig.setAllowCredentials(true);

    // Aplicar configuração em todas as rotas
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsFilter(source);
  }
}

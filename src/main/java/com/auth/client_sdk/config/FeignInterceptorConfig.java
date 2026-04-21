package com.auth.client_sdk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth.client_sdk.service.BotAuthService;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignInterceptorConfig {

    private final BotAuthService botAuthService;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 1. REGLA DE ORO: No interceptar la propia petición de Login del Bot
            // Si la ruta contiene "/login", se permite salir sin token
            if (template.url().contains("/login")) {
                log.debug("Petición de login detectada. Dejando pasar sin token...");
                return; 
            }
            try {
                String token = botAuthService.getValidToken();
                if (token != null && !token.isEmpty()) {
                    template.header("Authorization", "Bearer " + token);
                    log.debug("🛡Token inyectado exitosamente en la petición hacia: {}", template.url());
                }
            } catch (Exception e) {
                log.error("⚠Error crítico: No se pudo inyectar el token en Feign. {}", e.getMessage());
            }
        };
    }
}
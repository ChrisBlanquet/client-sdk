package com.auth.client_sdk.service;

import com.auth.client_sdk.client.LoginClient;
import com.auth.client_sdk.dto.LoginRequest;
import com.auth.client_sdk.dto.TokenResponse;
import com.ayuntamiento.security_lib.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotAuthService {

    private final LoginClient loginClient;
    private final JwtTokenProvider jwtProvider;

    @Value("${bot.security.username}")
    private String botUsername;

    @Value("${bot.security.password}")
    private String botPassword;

    private String currentToken = null;

    /**
     * Entrega un token vigente. Si no hay o expiró, lo renueva automáticamente.
     */
    public String getValidToken() {
        if (currentToken == null || !jwtProvider.validarToken(currentToken)) {
            log.info("🔐 Token de Bot vacío o expirado. Renovando...");
            renovarToken();
        }
        return currentToken;
    }

    /**
     * Proceso de autenticación interna vía OpenFeign
     */
    private void renovarToken() {
        try {
            LoginRequest request = LoginRequest.builder()
                    .email(botUsername.trim())
                    .password(botPassword.trim())
                    .build();

            TokenResponse response = loginClient.login(request);
            
            if (response != null && response.accessToken() != null) {
                this.currentToken = response.accessToken();
                log.info("Bot de Sistema: Token renovado exitosamente.");
            }
        } catch (Exception e) {
            log.error("Error Crítico: El Bot no pudo autenticarse con el micro de Auth. {}", e.getMessage());
            this.currentToken = null;
            throw new RuntimeException("Fallo en la autenticación del Bot SDK");
        }
    }

    /**
     * Permite invalidar el token actual manualmente si otro servicio detecta un 401
     */
    public void forzarRenovacion() {
        log.warn("Forzando la invalidación del token del Bot...");
        this.currentToken = null;
    }
}
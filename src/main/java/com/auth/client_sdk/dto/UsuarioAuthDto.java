package com.auth.client_sdk.dto;

import lombok.Builder;

@Builder
public record UsuarioAuthDto(
    Long id, 
    String username, 
    String rol
) {}
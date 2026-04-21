package com.auth.client_sdk.dto;

import lombok.Builder;

@Builder
public record UsuarioAuthDto(
	Long id,
	String email, 
    String nombre, 
    String rol,
    String telefono
) {}
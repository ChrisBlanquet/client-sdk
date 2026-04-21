package com.auth.client_sdk.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.auth.client_sdk.dto.UsuarioAuthDto;

@FeignClient(name = "AUTH", contextId = "usuarioClient", path = "/api/auth/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioAuthDto obtenerUsuario(@PathVariable("id") Long id);
    
    @GetMapping
    List<UsuarioAuthDto> obtenerTodosLosUsuarios();
}
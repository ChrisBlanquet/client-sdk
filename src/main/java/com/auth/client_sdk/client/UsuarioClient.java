package com.auth.client_sdk.client;

import com.auth.client_sdk.dto.UsuarioAuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AUTH", contextId = "usuarioClient", path = "/api/auth/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioAuthDto obtenerUsuario(@PathVariable("id") Long id);

}
package com.auth.client_sdk.client;

import com.auth.client_sdk.dto.LoginRequest;
import com.auth.client_sdk.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH", contextId = "loginClient", path = "/api/auth")
public interface LoginClient {

    @PostMapping("/login")
    TokenResponse login(@RequestBody LoginRequest request);

}
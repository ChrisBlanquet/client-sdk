package com.auth.client_sdk.dto;

import lombok.Builder;

@Builder
public record LoginRequest(
    String username, 
    String password
) {}
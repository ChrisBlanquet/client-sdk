package com.auth.client_sdk.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
    String token
) {}
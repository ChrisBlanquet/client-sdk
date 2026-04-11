package com.auth.client_sdk.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
// Le decimos a Spring que busque nuestros Beans (BotAuthService, Interceptor, etc.)
@ComponentScan(basePackages = "com.auth.client_sdk")
// Le decimos a Spring dónde están nuestras interfaces Feign
@EnableFeignClients(basePackages = "com.auth.client_sdk.client")
public class AuthSdkAutoConfiguration {

}
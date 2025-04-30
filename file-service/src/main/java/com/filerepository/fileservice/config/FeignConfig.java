package com.filerepository.fileservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.filerepository.fileservice.client")
public class FeignConfig {
    // This configuration enables Feign clients for service-to-service communication
}

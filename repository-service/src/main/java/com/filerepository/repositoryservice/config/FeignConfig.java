package com.filerepository.repositoryservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.filerepository.repositoryservice.client")
public class FeignConfig {
    // This configuration enables Feign clients
}

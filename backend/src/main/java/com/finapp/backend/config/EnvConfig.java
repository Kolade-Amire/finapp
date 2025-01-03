package com.finapp.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-dev.yml")
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class EnvConfig {
}

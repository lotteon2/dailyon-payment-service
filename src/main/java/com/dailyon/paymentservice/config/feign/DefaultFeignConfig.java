package com.dailyon.paymentservice.config.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> template.header("Content-Type", "application/json");
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return new ErrorDecoder.Default();
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}

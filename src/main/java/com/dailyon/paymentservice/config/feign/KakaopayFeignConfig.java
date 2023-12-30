package com.dailyon.paymentservice.config.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KakaopayFeignConfig {
  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> template.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
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

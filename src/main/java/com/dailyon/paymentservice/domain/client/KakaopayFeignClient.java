package com.dailyon.paymentservice.domain.client;

import com.dailyon.paymentservice.config.KakaopayFeignConfig;
import com.dailyon.paymentservice.domain.payment.paymanger.kakaopay.response.KakaopayReadyResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "kakaopayFeignClient",
    url = "https://kapi.kakao.com",
    configuration = KakaopayFeignConfig.class)
public interface KakaopayFeignClient {
  @PostMapping("/v1/payment/ready")
  KakaopayReadyResponseVO ready(
      @RequestHeader(value = "Authorization") String authorization, @RequestBody MultiValueMap map);
}

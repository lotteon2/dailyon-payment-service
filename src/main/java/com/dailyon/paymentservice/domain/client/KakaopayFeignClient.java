package com.dailyon.paymentservice.domain.client;

import com.dailyon.paymentservice.config.feign.KakaopayFeignConfig;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
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
  KakaopayDTO.ReadyDTO ready(
      @RequestHeader(value = "Authorization") String authorization,
      @RequestBody MultiValueMap data);

  @PostMapping("/v1/payment/approve")
  KakaopayDTO.ApproveDTO approve(
      @RequestHeader(value = "Authorization") String authorization,
      @RequestBody MultiValueMap data);

  @PostMapping("/v1/payment/cancel")
  KakaopayDTO.CancelDTO cancel(
      @RequestHeader(value = "Authorization") String authorization,
      @RequestBody MultiValueMap data);
}

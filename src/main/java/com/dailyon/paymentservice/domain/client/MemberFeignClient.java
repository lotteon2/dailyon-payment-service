package com.dailyon.paymentservice.domain.client;

import com.dailyon.paymentservice.config.feign.DefaultFeignConfig;
import com.dailyon.paymentservice.domain.client.dto.MemberPointUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "member-service",
    configuration = DefaultFeignConfig.class) // 임시 url
public interface MemberFeignClient {

  @PostMapping("/clients/members/points/add")
  ResponseEntity pointCharge(
      @RequestHeader(value = "memberId") Long memberId, @RequestBody MemberPointUpdateDTO request);
}

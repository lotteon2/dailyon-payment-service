package com.dailyon.paymentservice.domain.client;

import com.dailyon.paymentservice.config.MemberFeignConfig;
import com.dailyon.paymentservice.domain.client.request.MemberPointUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service", configuration = MemberFeignConfig.class) // 임시 url
public interface MemberFeignClient {

    @PostMapping
    void pointRecharge(@RequestHeader(value = "memberId") Long memberId, @RequestBody MemberPointUpdateRequest request);
}

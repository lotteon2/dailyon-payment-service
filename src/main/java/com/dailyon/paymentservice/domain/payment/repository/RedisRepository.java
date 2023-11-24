package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.domain.payment.paymanger.kakaopay.response.KakaopayReadyResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

  private final RedisTemplate<String, KakaopayReadyResponseVO> redisTemplate;

  public void saveReadyInfo(String key, KakaopayReadyResponseVO data) {
    redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(5));
  }
}

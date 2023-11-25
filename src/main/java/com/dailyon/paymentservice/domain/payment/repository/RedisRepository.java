package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

  private final RedisTemplate<String, KakaopayReadyDTO> redisTemplate;

  public void saveReadyInfo(String key, KakaopayReadyDTO data) {
    redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(5));
  }

  public Optional<KakaopayReadyDTO> findByOrderId(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }
}

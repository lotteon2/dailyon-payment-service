package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

  private final RedisTemplate<String, KakaopayDTO.ReadyDTO> redisTemplate;

  public void saveReadyInfo(String key, KakaopayDTO.ReadyDTO data) {
    redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(5));
  }

  public Optional<KakaopayDTO.ReadyDTO> findByOrderId(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }
}

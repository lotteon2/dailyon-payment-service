package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RedisRepositoryTest extends IntegrationTestSupport {
  @Autowired RedisRepository redisRepository;

  @DisplayName("결제 준비 상태의 결제 정보를 조회 한다.")
  @Test
  void findByOrderId() {
    // given
    KakaopayReadyDTO dto =
        KakaopayReadyDTO.builder()
            .tid("1")
            .nextRedirectAppUrl("testRedirectAppUrl")
            .androidAppScheme("testAnd")
            .iosAppScheme("testIos")
            .createdAt("2023-12-12")
            .nextRedirectMobileUrl("testRedirectMobilUrl")
            .nextRedirectPcUrl("testRedirectPcUrl")
            .tmsResult(true)
            .build();
    String orderId = OrderNoGenerator.generate(1L);
    redisRepository.saveReadyInfo(orderId, dto);
    // when
    KakaopayReadyDTO kakaopayReadyDTO = redisRepository.findByOrderId(orderId).get();
    // then
    assertThat(kakaopayReadyDTO)
        .isNotNull()
        .extracting("tid", "nextRedirectPcUrl")
        .containsExactly(kakaopayReadyDTO.getTid(), kakaopayReadyDTO.getNextRedirectPcUrl());
  }
}

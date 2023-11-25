package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.payment.api.request.PaymentReadyRequest;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.repository.RedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KakaoPayManagerTest {

  @Mock private KakaopayFeignClient kakaopayFeignClient;

  @Mock private RedisRepository redisRepository;

  @InjectMocks private KakaoPayManager kakaoPayManager;

  @DisplayName("포인트 결제 준비 테스트")
  @Test
  void pointPaymentReady() {
    // given
    KakaopayReadyDTO response =
        KakaopayReadyDTO.builder()
            .tid("tid")
            .tmsResult(true)
            .nextRedirectPcUrl("nextRedirectPcUrl")
            .nextRedirectMobileUrl("nextRedirectMobileUrl")
            .nextRedirectAppUrl("nextRedirectAppUrl")
            .androidAppScheme("androidAppScheme")
            .iosAppScheme("iosAppScheme")
            .createdAt("createdAt")
            .build();
    given(kakaopayFeignClient.ready(anyString(), any())).willReturn(response);
    PaymentReadyRequest.PointPaymentReadyRequest pointPaymentReadyRequest =
        new PaymentReadyRequest.PointPaymentReadyRequest(PaymentMethod.KAKAOPAY, 10000);
    // when
    KakaopayReadyDTO result = kakaoPayManager.ready(1L, "orderId", pointPaymentReadyRequest);
    // then
    assertThat(result).isNotNull();
    verify(redisRepository, times(1)).saveReadyInfo("orderId", result);
  }
}

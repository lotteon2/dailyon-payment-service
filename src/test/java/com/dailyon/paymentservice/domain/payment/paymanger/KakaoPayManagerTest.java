package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.repository.RedisRepository;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.*;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
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
    KakaopayDTO.ReadyDTO response =
        KakaopayDTO.ReadyDTO.builder()
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
    PointPaymentRequest.PointPaymentReadyRequest pointPaymentReadyRequest =
        new PointPaymentRequest.PointPaymentReadyRequest(KAKAOPAY, 10000);
    // when
    KakaopayDTO.ReadyDTO result =
        kakaoPayManager.ready(pointPaymentReadyRequest.toFacadeRequest(1L,"orderId"));
    // then
    assertThat(result).isNotNull();
    verify(redisRepository, times(1)).saveReadyInfo("orderId", result);
  }

  @DisplayName("포인트 결제 승인 테스트")
  @Test
  void pointPaymentApprove() {
    // given

    KakaopayDTO.ReadyDTO readyDTO =
        KakaopayDTO.ReadyDTO.builder()
            .tid("tid")
            .tmsResult(true)
            .nextRedirectPcUrl("nextRedirectPcUrl")
            .nextRedirectMobileUrl("nextRedirectMobileUrl")
            .nextRedirectAppUrl("nextRedirectAppUrl")
            .androidAppScheme("androidAppScheme")
            .iosAppScheme("iosAppScheme")
            .createdAt("createdAt")
            .build();

    String orderId = OrderNoGenerator.generate(1L);
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime approvedAt = LocalDateTime.now();
    KakaopayDTO.Amount amount = KakaopayDTO.Amount.builder().total(250000).build();

    KakaopayDTO.ApproveDTO approveDTO =
        KakaopayDTO.ApproveDTO.builder()
            .aid("testAid")
            .cid("cid")
            .tid("tid")
            .itemName(POINT.getMessage())
            .createdAt(createdAt)
            .approvedAt(approvedAt)
            .amount(amount)
            .paymentMethod("CARD")
            .orderId(orderId)
            .quantity(1)
            .userId("1")
            .build();

    given(kakaopayFeignClient.approve(anyString(), any())).willReturn(approveDTO);
    given(redisRepository.findByOrderId(orderId)).willReturn(Optional.ofNullable(readyDTO));
    PointPaymentRequest.PointPaymentApproveRequest request =
        new PointPaymentRequest.PointPaymentApproveRequest("pgToken");
    // when
    KakaopayDTO.ApproveDTO result = kakaoPayManager.approve(request.toFacadeRequest(orderId,KAKAOPAY));
    // then
    assertThat(result).isNotNull();
    verify(kakaopayFeignClient, times(1)).approve(anyString(), any());
    verify(redisRepository, times(1)).findByOrderId(orderId);
  }
}

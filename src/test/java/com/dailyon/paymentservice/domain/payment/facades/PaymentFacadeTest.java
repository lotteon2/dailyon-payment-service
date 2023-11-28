package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PaymentFacadeTest extends IntegrationTestSupport {
  @Autowired PaymentFacade paymentFacade;
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;

  @AfterEach
  void tearDown() {
    kakaopayInfoRepository.deleteAllInBatch();
    paymentRepository.deleteAllInBatch();
  }

  @DisplayName("주문번호와 pgToken을 받아 결제 승인을 요청한다.")
  @Test
  void pointPaymentApprove() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(1L);
    PointPaymentRequest.PointPaymentApproveRequest request =
        new PointPaymentRequest.PointPaymentApproveRequest(orderId, "pgToken");
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
    given(kakaoPayManager.approve(memberId, request)).willReturn(approveDTO);
    given(memberFeignClient.pointCharge(any(), any())).willReturn(ResponseEntity.ok().build());
    // when
    Long paymentId = paymentFacade.pointPaymentApprove(memberId, request);
    // then
    Payment getPayment = paymentRepository.findById(paymentId).get();
    assertThat(getPayment.getId()).isNotNull().isEqualTo(paymentId);
    verify(kakaoPayManager, times(1)).approve(any(), any());
    verify(memberFeignClient, times(1)).pointCharge(any(), any());
  }
}

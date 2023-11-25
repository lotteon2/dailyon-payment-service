package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest extends IntegrationTestSupport {
  @Autowired PaymentService paymentService;
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;

  @AfterEach
  void tearDown() {
    kakaopayInfoRepository.deleteAllInBatch();
    paymentRepository.deleteAllInBatch();
  }

  @DisplayName("결제 정보와 카카오 결제 아이디를 입력받아 결제내역을 생성한다.")
  @Test
  void createPointPayment() {
    // given
    CreatePaymentServiceRequest request = CreatePaymentServiceRequest.builder()
            .totalAmount(1000)
            .memberId(1L)
            .method(KAKAOPAY)
            .type(POINT)
            .build();
    String tid = "1020213";
    // when
    Long paymentId = paymentService.createPayment(request, tid);
    // then
    Payment getPayment = paymentRepository.findById(paymentId).get();
    assertThat(paymentId).isNotNull().isEqualTo(getPayment.getId());
  }

  private Payment createPayment(
      Long memberId, PaymentMethod method, PaymentType type, Integer totalAmount) {
    return Payment.builder()
        .method(method)
        .type(type)
        .status(COMPLETED)
        .totalAmount(totalAmount)
        .memberId(memberId)
        .build();
  }
}

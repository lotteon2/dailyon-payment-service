package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PaymentServiceTest extends IntegrationTestSupport {
  @Autowired PaymentService paymentService;
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;
  @Autowired EntityManager entityManager;

  @DisplayName("결제 정보와 카카오 결제 아이디를 입력받아 결제내역을 생성한다.")
  @Test
  void createPointPayment() {
    // given
    CreatePaymentServiceRequest request =
        CreatePaymentServiceRequest.builder()
            .totalAmount(1000)
            .memberId(1L)
            .orderNo("testOrderNo")
            .method(KAKAOPAY)
            .type(POINT)
            .build();
    String tid = "1020213";
    // when
    Long paymentId = paymentService.createPayment(request, tid);
    entityManager.flush();
    entityManager.clear();
    //     then
    Payment getPayment = paymentRepository.findById(paymentId).get();
    assertThat(paymentId).isNotNull().isEqualTo(getPayment.getId());
  }

  @DisplayName("포인트 결제 내역을 8개씩 조회한다.")
  @Test
  void getPayments() {
    // given
    Long memberId = 1L;
    PaymentMethod method = KAKAOPAY;
    PaymentType type = POINT;
    Long paymentId = null;
    for (int i = 1; i <= 9; i++) {
      Payment save = paymentRepository.save(createPayment(memberId, method, type, 2000 * i));
    }
    Pageable page = PageRequest.of(0, 8);
    // when
    Page<Payment> payments = paymentService.getPayments(page, memberId, type);
    // then
    assertThat(payments).isNotNull();
    assertThat(payments.getTotalElements()).isEqualTo(9);
    assertThat(payments.getTotalPages()).isEqualTo(2);
  }

  private KakaopayInfo createKakaoPayInfo(Payment payment, String tid) {
    return KakaopayInfo.builder().tid(tid).payment(payment).build();
  }

  private Payment createPayment(
      Long memberId, PaymentMethod method, PaymentType type, Integer totalAmount) {
    return Payment.builder()
        .method(method)
        .orderNo(OrderNoGenerator.generate(memberId))
        .type(type)
        .status(COMPLETED)
        .totalAmount(totalAmount)
        .memberId(memberId)
        .build();
  }
}

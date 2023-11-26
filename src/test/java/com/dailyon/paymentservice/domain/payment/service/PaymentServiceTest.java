package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.exception.AuthorizationException;
import com.dailyon.paymentservice.domain.payment.exception.PaymentNotFoundException;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.OrderPaymentInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import com.dailyon.paymentservice.domain.payment.service.response.OrderPaymentResponse;
import com.dailyon.paymentservice.domain.payment.service.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.ORDER;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class PaymentServiceTest extends IntegrationTestSupport {
  @Autowired PaymentService paymentService;
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;
  @Autowired OrderPaymentInfoRepository orderPaymentInfoRepository;
  @Autowired EntityManager entityManager;

  @DisplayName("결제 정보와 카카오 결제 아이디를 입력받아 결제내역을 생성한다.")
  @Test
  void createPointPayment() {
    // given
    CreatePaymentServiceRequest request =
        CreatePaymentServiceRequest.builder()
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
    PaymentPageResponse payments = paymentService.getPayments(page, memberId, paymentId, type);
    // then
    assertThat(payments).isNotNull();
    assertThat(payments.getPayments()).isNotEmpty().hasSize(8);
    assertThat(payments.isHasNext()).isTrue();
  }

  @DisplayName("주문 번호로 해당 주문의 결제 내역을 조회한다.")
  @Test
  void getOrderPayment() {
    // given
    Long memberId = 1L;
    PaymentMethod method = KAKAOPAY;
    PaymentType type = ORDER;
    String orderId = OrderNoGenerator.generate(memberId);
    Payment payment = createPayment(memberId, method, type, 65000);
    paymentRepository.save(payment);
    OrderPaymentInfo orderPaymentInfo = createOrderPaymentInfo(payment, orderId);
    orderPaymentInfoRepository.save(orderPaymentInfo);
    entityManager.flush();
    entityManager.clear();
    // when
    OrderPaymentResponse response = paymentService.getOrderPayment(orderId, memberId);
    // then
    assertThat(response).isNotNull();
    assertThat(response)
        .extracting("deliveryFee", "method", "totalAmount")
        .containsExactly(
            response.getDeliveryFee(), response.getMethod(), response.getTotalAmount());
  }

  @DisplayName("조회 하려는 결제 내역의 주문이 존재 하지 않는 경우 예외가 발생한다.")
  @Test
  void getOrderPaymentWithNoExistOrderId() {
    // given
    Long memberId = 1L;
    String NoExistOrderId = "orderId";
    // when // then
    assertThatThrownBy(() -> paymentService.getOrderPayment(NoExistOrderId, memberId))
        .isInstanceOf(PaymentNotFoundException.class)
        .hasMessage("결제 정보가 존재하지 않습니다.");
  }

  @DisplayName("다른 유저의 주문 결제 내역을 조회하려고 할 시 예외가 발생한다.")
  @Test
  void getOrderPaymentNoAuth() {
    // given
    Long memberId = 1L;
    PaymentMethod method = KAKAOPAY;
    PaymentType type = ORDER;
    String orderId = OrderNoGenerator.generate(memberId);
    Payment payment = createPayment(memberId, method, type, 65000);
    paymentRepository.save(payment);
    OrderPaymentInfo orderPaymentInfo = createOrderPaymentInfo(payment, orderId);
    orderPaymentInfoRepository.save(orderPaymentInfo);
    entityManager.flush();
    entityManager.clear();
    Long otherMemberId = 2L;
    // when // then
    assertThatThrownBy(() -> paymentService.getOrderPayment(orderId, otherMemberId))
            .isInstanceOf(AuthorizationException.class)
            .hasMessage("권한이 없습니다.");
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

  private OrderPaymentInfo createOrderPaymentInfo(Payment payment, String orderId) {
    return OrderPaymentInfo.builder()
        .orderId(orderId)
        .payment(payment)
        .usedPoints(0)
        .deliveryFee(3000)
        .totalCouponDiscountAmount(0)
        .build();
  }
}

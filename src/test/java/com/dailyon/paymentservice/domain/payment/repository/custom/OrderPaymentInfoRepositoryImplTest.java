package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.repository.OrderPaymentInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;

class OrderPaymentInfoRepositoryImplTest extends IntegrationTestSupport {
  @Autowired PaymentRepository paymentRepository;
  @Autowired OrderPaymentInfoRepository orderPaymentInfoRepository;

  @Transactional
  @DisplayName("주문 번호로 주문 결제 정보를 조회한다.")
  @Test
  void getOrderPaymentInfo() {
    // given
    Integer totalAmount = 350000;
    Payment payment = createPayment(350000);
    paymentRepository.save(payment);
    String orderId = OrderNoGenerator.generate(1L);
    OrderPaymentInfo orderPaymentInfo = createOrderPaymentInfo(payment, orderId);
    orderPaymentInfoRepository.save(orderPaymentInfo);
    // when
    OrderPaymentInfo getOrderPayInfo = orderPaymentInfoRepository.findByOrderIdFetch(orderId).get();
    // then
    assertThat(getOrderPayInfo)
        .isNotNull()
        .extracting("orderId", "deliveryFee")
        .containsExactly(orderId, orderPaymentInfo.getDeliveryFee());
  }

  private Payment createPayment(Integer totalAmount) {
    return Payment.builder()
        .type(POINT)
        .method(KAKAOPAY)
        .status(COMPLETED)
        .totalAmount(totalAmount)
        .memberId(1L)
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

package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.OrderPaymentInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.ORDER;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PaymentRepositoryImplTest extends IntegrationTestSupport {
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;
  @Autowired OrderPaymentInfoRepository orderPaymentInfoRepository;
  @Autowired EntityManager em;

  @DisplayName("회원의 포인트 결제 내역을 조회한다. 페이지 수는 8개씩이다.")
  @Test
  void findAllByMemberIdPaging() {
    // given
    for (int i = 1; i <= 9; i++) {
      Payment save = paymentRepository.save(createPayment(2000 * i, POINT));
    }
    Pageable page = PageRequest.of(0, 8);
    // when
    Slice<Payment> payments = paymentRepository.findByMemberId(page, 1L, null, POINT);
    // then
    assertThat(payments).isNotNull();
    assertThat(payments.getContent()).isNotEmpty().hasSize(8);
    assertThat(payments.hasNext()).isTrue();
  }

  @DisplayName("주문 번호로 주문 결제 정보를 조회한다.")
  @Test
  void getOrderPaymentInfo() {
    // given
    Integer totalAmount = 350000;
    String orderId = OrderNoGenerator.generate(1L);
    Payment payment = createPayment(totalAmount, ORDER);
    KakaopayInfo kakaopayInfo = createKakaoPayInfo(payment, "tid");
    paymentRepository.save(payment);
    kakaopayInfoRepository.save(kakaopayInfo);
    OrderPaymentInfo orderPaymentInfo = createOrderPaymentInfo(payment, orderId);
    orderPaymentInfoRepository.save(orderPaymentInfo);
    em.flush();
    em.clear();
    // when
    Payment getPayment = paymentRepository.findByOrderIdFetch(orderId).get();
    // then
    assertThat(getPayment.getOrderPaymentInfo())
        .isNotNull()
        .extracting("orderId", "deliveryFee")
        .containsExactly(orderId, getPayment.getOrderPaymentInfo().getDeliveryFee());
  }

  private KakaopayInfo createKakaoPayInfo(Payment payment, String tid) {
    return KakaopayInfo.builder().tid(tid).payment(payment).build();
  }

  private Payment createPayment(Integer totalAmount, PaymentType type) {
    return Payment.builder()
        .type(type)
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

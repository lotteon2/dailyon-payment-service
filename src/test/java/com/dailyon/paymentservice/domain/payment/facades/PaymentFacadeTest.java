package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.payment.api.request.OrderPaymentRequest;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.OrderPaymentInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.ORDER;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
class PaymentFacadeTest extends IntegrationTestSupport {
  @Autowired PaymentFacade paymentFacade;
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;
  @Autowired OrderPaymentInfoRepository orderPaymentInfoRepository;
  @Autowired EntityManager entityManager;

  @AfterEach
  void tearDown() {
    kakaopayInfoRepository.deleteAllInBatch();
    orderPaymentInfoRepository.deleteAllInBatch();
    paymentRepository.deleteAllInBatch();
  }

  @DisplayName("주문번호와 pgToken을 받아 결제 승인을 요청한다.")
  @Test
  void pointPaymentApprove() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(1L);
    PointPaymentRequest.PointPaymentApproveRequest approveRequest =
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

    PaymentFacadeRequest.PaymentApproveRequest request = approveRequest.toFacadeRequest(memberId, KAKAOPAY);
    given(kakaoPayManager.approve(request)).willReturn(approveDTO);
    given(memberFeignClient.pointCharge(any(), any())).willReturn(ResponseEntity.ok().build());
    // when
    Long paymentId = paymentFacade.paymentApprove(request);
    // then
    Payment getPayment = paymentRepository.findById(paymentId).get();
    assertThat(getPayment.getId()).isNotNull().isEqualTo(paymentId);
    verify(kakaoPayManager, times(1)).approve(any());
    verify(memberFeignClient, times(1)).pointCharge(any(), any());
  }

  @DisplayName("주문 상세에 대한 정보를 받아 카카오페이 부분 취소한다.")
  @Test
  void OrderPaymentKakaopayCancel() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(memberId);
    OrderPaymentRequest.OrderPaymentCancelRequest request =
        new OrderPaymentRequest.OrderPaymentCancelRequest(orderId, 25000);
    Payment payment = createPayment(memberId, KAKAOPAY, ORDER, 65000);
    KakaopayInfo kakaopayInfo = createKakaoPayInfo(payment, "tid");
    paymentRepository.save(payment);
    kakaopayInfoRepository.save(kakaopayInfo);
    OrderPaymentInfo orderPaymentInfo = createOrderPaymentInfo(payment, orderId);
    orderPaymentInfoRepository.save(orderPaymentInfo);
    entityManager.flush();
    entityManager.clear();

    KakaopayDTO.CancelDTO response = KakaopayDTO.CancelDTO.builder().aid("a").build();
    given(kakaoPayManager.cancel(any(), any(), any())).willReturn(response);
    // when
    paymentFacade.cancel(memberId, request);
    // then
    verify(kakaoPayManager, times(1)).cancel(orderId, request.getCancelAmount(), memberId);
  }

  private KakaopayInfo createKakaoPayInfo(Payment payment, String tid) {
    return KakaopayInfo.builder().tid(tid).payment(payment).build();
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

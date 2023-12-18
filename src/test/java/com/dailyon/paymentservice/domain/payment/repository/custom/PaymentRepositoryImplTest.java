package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.IntegrationTestSupport;
import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
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
class PaymentRepositoryImplTest extends IntegrationTestSupport {
  @Autowired PaymentRepository paymentRepository;
  @Autowired KakaopayInfoRepository kakaopayInfoRepository;
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
    Page<Payment> payments = paymentRepository.findByMemberId(page, 1L, POINT);
    // then
    assertThat(payments).isNotNull();
    assertThat(payments.getTotalElements()).isEqualTo(9);
    assertThat(payments.getTotalPages()).isEqualTo(2);
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

}

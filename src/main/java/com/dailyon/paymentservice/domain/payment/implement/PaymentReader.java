package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.exception.AuthorizationException;
import com.dailyon.paymentservice.domain.payment.exception.PaymentNotFoundException;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentReader {
  private final PaymentRepository paymentRepository;

  public Page<Payment> read(Pageable pageable, Long memberId, PaymentType type) {
    return paymentRepository.findByMemberId(pageable, memberId, type);
  }

  public Payment read(String orderId, Long memberId) {
    Payment payment =
        paymentRepository.findByOrderIdFetch(orderId).orElseThrow(PaymentNotFoundException::new);
    isValidAuth(payment, memberId);
    return payment;
  }

  public Payment readKakao(String orderId, Long memberId) {
    Payment payment =
        paymentRepository
            .findKakaoPaymentByOrderId(orderId)
            .orElseThrow(PaymentNotFoundException::new);
    isValidAuth(payment, memberId);
    return payment;
  }

  private void isValidAuth(Payment payment, Long memberId) {
    if (payment.getMemberId() != memberId) {
      throw new AuthorizationException();
    }
  }
}

package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.exception.AuthorizationException;
import com.dailyon.paymentservice.domain.payment.exception.PaymentNotFoundException;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentReader {
  private final PaymentRepository paymentRepository;

  public Slice<Payment> read(Pageable pageable, Long memberId, Long paymentId, PaymentType type) {
    return paymentRepository.findByMemberId(pageable, memberId, paymentId, type);
  }

  public Payment read(String orderId, Long memberId) {
    Payment payment =
        paymentRepository.findByOrderIdFetch(orderId).orElseThrow(PaymentNotFoundException::new);
    if (!isValidAuth(payment, memberId)) {
      throw new AuthorizationException();
    }
    return payment;
  }

  private boolean isValidAuth(Payment payment, Long memberId) {
    return payment.getMemberId() == memberId;
  }
}

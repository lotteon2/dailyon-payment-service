package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.exception.AuthorizationException;
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

  private void isValidAuth(Payment payment, Long memberId) {
    if (payment.getMemberId() != memberId) {
      throw new AuthorizationException();
    }
  }
}

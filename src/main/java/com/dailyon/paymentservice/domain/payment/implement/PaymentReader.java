package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
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
}

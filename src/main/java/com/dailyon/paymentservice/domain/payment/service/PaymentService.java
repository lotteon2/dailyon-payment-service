package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.implement.PaymentAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
  private final PaymentAppender paymentAppender;

  @Transactional
  public Long createPayment(Payment payment, String tid) {
    Long paymentId = paymentAppender.append(payment, tid);
    return paymentId;
  }
}

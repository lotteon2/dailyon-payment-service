package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.repository.OrderPaymentInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentAppender {
  private final PaymentRepository paymentRepository;
  private final OrderPaymentInfoRepository orderPaymentInfoRepository;

  public void append(Payment payment) {
    paymentRepository.save(payment);
  }
}

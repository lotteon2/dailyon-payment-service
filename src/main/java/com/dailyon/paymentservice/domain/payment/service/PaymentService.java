package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.implement.PaymentAppender;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
  private final PaymentAppender paymentAppender;

  @Transactional
  public Long createPayment(CreatePaymentServiceRequest request, String payId) {
    Payment payment = request.toEntity();
    Long paymentId = paymentAppender.append(payment, payId);
    return paymentId;
  }
}

package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.implement.PaymentAppender;
import com.dailyon.paymentservice.domain.payment.implement.PaymentReader;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import com.dailyon.paymentservice.domain.payment.service.response.PaymentPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
  private final PaymentAppender paymentAppender;
  private final PaymentReader paymentReader;

  @Transactional
  public Long createPayment(CreatePaymentServiceRequest request, String payId) {
    Payment payment = request.toEntity();
    Long paymentId = paymentAppender.append(payment, payId);
    return paymentId;
  }

  public PaymentPageResponse getPayments(
      Pageable pageable, Long memberId, Long paymentId, PaymentType type) {
    Slice<Payment> slice = paymentReader.read(pageable, memberId, paymentId, type);
    return PaymentPageResponse.from(slice);
  }
}

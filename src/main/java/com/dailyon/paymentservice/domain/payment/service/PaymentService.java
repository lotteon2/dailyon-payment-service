package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.implement.PaymentAppender;
import com.dailyon.paymentservice.domain.payment.implement.PaymentReader;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Page<Payment> getPayments(Pageable pageable, Long memberId, PaymentType type) {
    Page<Payment> page = paymentReader.read(pageable, memberId, type);
    return page;
  }

  public Payment getOrderPayment(String orderNo) {
    return paymentReader.readByOrderNo(orderNo);
  }
}

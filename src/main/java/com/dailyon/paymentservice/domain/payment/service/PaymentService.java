package com.dailyon.paymentservice.domain.payment.service;

import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.implement.PaymentAppender;
import com.dailyon.paymentservice.domain.payment.implement.PaymentReader;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
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

  @Transactional
  public Long createOrderPayment(
      CreatePaymentServiceRequest request, String orderId, String payId) {
    OrderPaymentInfo orderPaymentInfo = request.toOrderPaymentEntity(orderId);
    Long paymentId = paymentAppender.append(orderPaymentInfo, payId);
    return paymentId;
  }

  public Slice<Payment> getPayments(
      Pageable pageable, Long memberId, Long paymentId, PaymentType type) {
    Slice<Payment> slice = paymentReader.read(pageable, memberId, paymentId, type);
    return slice;
  }

  public Payment getOrderPayment(String orderId, Long memberId) {
    Payment payment = paymentReader.read(orderId, memberId);
    return payment;
  }
}

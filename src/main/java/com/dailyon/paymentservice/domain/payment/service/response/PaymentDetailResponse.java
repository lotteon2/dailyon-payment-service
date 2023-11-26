package com.dailyon.paymentservice.domain.payment.service.response;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentDetailResponse {

  private PaymentStatus status;
  private PaymentMethod method;
  private Integer totalAmount;
  private String createdAt;

  @Builder
  private PaymentDetailResponse(
      PaymentStatus status, PaymentMethod method, Integer totalAmount, String createdAt) {
    this.status = status;
    this.method = method;
    this.totalAmount = totalAmount;
    this.createdAt = createdAt;
  }

  public static PaymentDetailResponse from(Payment payment) {
    return PaymentDetailResponse.builder()
        .method(payment.getMethod())
        .status(payment.getStatus())
        .totalAmount(payment.getTotalAmount())
        .createdAt(payment.getCreatedAt().toString())
        .build();
  }
}

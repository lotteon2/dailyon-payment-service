package com.dailyon.paymentservice.domain.payment.service.request;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus.COMPLETED;

@Getter
@NoArgsConstructor
public class CreatePaymentServiceRequest {
  private Long memberId;
  private PaymentType type; // ORDER, POINT
  private PaymentMethod method; // KAKAOPAY
  private Integer totalAmount;

  @Builder
  private CreatePaymentServiceRequest(
      Long memberId, PaymentType type, PaymentMethod method, Integer totalAmount) {
    this.memberId = memberId;
    this.type = type;
    this.method = method;
    this.totalAmount = totalAmount;
  }

  public Payment toEntity() {
    return Payment.builder()
        .memberId(memberId)
        .type(type)
        .status(COMPLETED)
        .method(method)
        .totalAmount(totalAmount)
        .build();
  }
}

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

  private int quantity;
  private int deliveryFee;
  private int totalCouponDiscountPrice;
  private int usedPoints;

  @Builder
  private CreatePaymentServiceRequest(
      Long memberId,
      PaymentType type,
      PaymentMethod method,
      Integer totalAmount,
      int quantity,
      int deliveryFee,
      int totalCouponDiscountPrice,
      int usedPoints) {
    this.memberId = memberId;
    this.type = type;
    this.method = method;
    this.totalAmount = totalAmount;
    this.quantity = quantity;
    this.deliveryFee = deliveryFee;
    this.totalCouponDiscountPrice = totalCouponDiscountPrice;
    this.usedPoints = usedPoints;
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

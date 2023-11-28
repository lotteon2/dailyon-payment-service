package com.dailyon.paymentservice.domain.payment.facades.response;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderPaymentResponse {

  private int totalCouponDiscountPrice;
  private int deliveryFee;
  private int usedPoints;
  private PaymentStatus status;
  private PaymentMethod method;
  private Integer totalAmount;
  private String createdAt;

  @Builder
  private OrderPaymentResponse(
      Integer totalCouponDiscountPrice,
      Integer deliveryFee,
      Integer usedPoints,
      PaymentStatus status,
      PaymentMethod method,
      Integer totalAmount,
      String createdAt) {
    this.totalCouponDiscountPrice = totalCouponDiscountPrice;
    this.deliveryFee = deliveryFee;
    this.usedPoints = usedPoints;
    this.status = status;
    this.method = method;
    this.totalAmount = totalAmount;
    this.createdAt = createdAt;
  }

  public static OrderPaymentResponse from(Payment payment) {
    return OrderPaymentResponse.builder()
        .totalCouponDiscountPrice(payment.getOrderPaymentInfo().getTotalCouponDiscountAmount())
        .deliveryFee(payment.getOrderPaymentInfo().getDeliveryFee())
        .usedPoints(payment.getOrderPaymentInfo().getUsedPoints())
        .method(payment.getMethod())
        .status(payment.getStatus())
        .totalAmount(payment.getTotalAmount())
        .createdAt(payment.getCreatedAt().toString())
        .build();
  }
}

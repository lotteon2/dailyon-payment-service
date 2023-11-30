package com.dailyon.paymentservice.domain.payment.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(name = "idx_order_id", columnList = "orderId"))
public class OrderPaymentInfo {
  @Id private Long paymentId;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @NotNull private String orderId;

  @Column(nullable = true)
  private int totalCouponDiscountAmount;

  @Column(nullable = true)
  private int deliveryFee;

  @Column(nullable = true)
  private int usedPoints;

  @Builder
  private OrderPaymentInfo(
      Payment payment,
      String orderId,
      int totalCouponDiscountAmount,
      int deliveryFee,
      int usedPoints) {
    this.payment = payment;
    this.orderId = orderId;
    this.totalCouponDiscountAmount = totalCouponDiscountAmount;
    this.deliveryFee = deliveryFee;
    this.usedPoints = usedPoints;
  }
}

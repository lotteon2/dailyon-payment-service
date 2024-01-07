package com.dailyon.paymentservice.domain.payment.facades.request;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentFacadeRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaymentReadyRequest {
    private Long memberId;
    private String orderId;
    private String productName;
    private PaymentType type; // ORDER, POINT
    private PaymentMethod method; // KAKAOPAY
    private Integer totalAmount;

    private int quantity;
    private int deliveryFee;
    private int totalCouponDiscountPrice;
    private int usedPoints;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaymentApproveRequest {

    private String orderId;
    private String pgToken;
    private PaymentType type;
    private PaymentMethod method;

    private int quantity;
    private int deliveryFee;
    private int totalCouponDiscountPrice;
    private int usedPoints;

    public CreatePaymentServiceRequest toServiceRequest() {
      return CreatePaymentServiceRequest.builder()
          .orderNo(orderId)
          .quantity(quantity)
          .deliveryFee(deliveryFee)
          .totalAmount(totalCouponDiscountPrice)
          .usedPoints(usedPoints)
          .type(type)
          .method(method)
          .build();
    }
  }
}

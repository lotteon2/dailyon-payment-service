package com.dailyon.paymentservice.domain.payment.api.request;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.ORDER;

public class OrderPaymentRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class OrderPaymentCancelRequest {

    @NotNull(message = "주문번호는 필수 입니다.")
    private String orderId;

    @NotNull(message = "취소할 금액은 필수 입니다.")
    private Integer cancelAmount;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class OrderPaymentReadyRequest {
    @NotNull(message = "주문번호는 필수 입니다.")
    private String orderId;

    @NotNull(message = "결제 수단은 필수 입니다.")
    private PaymentMethod method;

    @NotNull(message = "상품 이름은 필수 입니다.")
    private String productName;

    @NotNull(message = "수량은 필수 입니다.")
    private Integer quantity;

    @NotNull(message = "결제 금액은 필수 입니다.")
    private Integer totalAmount;

    private int deliveryFee;
    private int totalCouponDiscountPrice;
    private int usedPoints;

    public PaymentFacadeRequest.PaymentReadyRequest toFacadeRequest(Long memberId) {
      return PaymentFacadeRequest.PaymentReadyRequest.builder()
          .memberId(memberId)
          .orderId(orderId)
          .type(ORDER)
          .method(method)
          .productName(productName)
          .quantity(quantity)
          .totalAmount(totalAmount)
          .deliveryFee(deliveryFee)
          .totalCouponDiscountPrice(totalCouponDiscountPrice)
          .usedPoints(usedPoints)
          .build();
    }
  }
}

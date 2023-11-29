package com.dailyon.paymentservice.domain.payment.api.request;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;

public class PointPaymentRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PointPaymentReadyRequest {

    @NotNull(message = "결제 수단은 필수 입니다.")
    private PaymentMethod method;

    @NotNull(message = "결제 금액은 필수 입니다.")
    private Integer totalAmount;

    public PaymentFacadeRequest.PaymentReadyRequest toFacadeRequest(Long memberId, String orderId) {
      return PaymentFacadeRequest.PaymentReadyRequest.builder()
          .memberId(memberId)
          .orderId(orderId)
          .type(POINT)
          .method(method)
          .totalAmount(totalAmount)
          .productName(POINT.getMessage())
          .quantity(1)
          .deliveryFee(0)
          .usedPoints(0)
          .totalCouponDiscountPrice(0)
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PointPaymentApproveRequest {

    @NotEmpty(message = "주문번호는 필수 입니다.")
    private String orderId;

    @NotNull(message = "pgToken은 필수 입니다.")
    private String pgToken;
  }
}

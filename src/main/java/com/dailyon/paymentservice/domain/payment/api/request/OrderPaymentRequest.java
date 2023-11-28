package com.dailyon.paymentservice.domain.payment.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class OrderPaymentRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderPaymentCancelRequest {

    private String orderId;

    private Long orderDetailId;

    @NotNull(message = "취소할 금액은 필수 입니다.")
    private Integer cancelAmount;
  }
}

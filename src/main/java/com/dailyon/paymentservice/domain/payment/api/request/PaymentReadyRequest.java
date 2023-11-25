package com.dailyon.paymentservice.domain.payment.api.request;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class PaymentReadyRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PointPaymentReadyRequest {

    @NotNull(message = "결제 수단은 필수 입니다.")
    private PaymentMethod method;

    @NotNull(message = "결제 금액은 필수 입니다.")
    private Integer totalAmount;
  }
}

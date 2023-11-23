package com.dailyon.paymentservice.domain.payment.api.request;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentReadyRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PointPaymentReadyRequest {
    private PaymentMethod method;
    private Integer totalAmount;
  }
}

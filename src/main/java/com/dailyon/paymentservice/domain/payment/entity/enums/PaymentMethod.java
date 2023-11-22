package com.dailyon.paymentservice.domain.payment.entity.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
  KAKAOPAY("카카오페이");

  private final String message;

  PaymentMethod(String message) {
    this.message = message;
  }
}

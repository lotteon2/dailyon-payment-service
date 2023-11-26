package com.dailyon.paymentservice.domain.payment.entity.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
  ORDER("주문결제"),
  POINT("포인트충전결제");

  private final String message;

  PaymentType(String message) {
    this.message = message;
  }
}

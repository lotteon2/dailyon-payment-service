package com.dailyon.paymentservice.domain.payment.entity.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
  READY("결제준비"),
  FAIL("결제실패"),
  CANCEL("결제취소"),
  COMPLETED("결제완료");

  private final String message;

  PaymentStatus(String message) {
    this.message = message;
  }
}

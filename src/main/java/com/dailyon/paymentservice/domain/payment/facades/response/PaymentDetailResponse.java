package com.dailyon.paymentservice.domain.payment.facades.response;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentDetailResponse {

  private String status;
  private String method;
  private Integer totalAmount;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime createdAt;

  @Builder
  private PaymentDetailResponse(
      String status, String method, Integer totalAmount, LocalDateTime createdAt) {
    this.status = status;
    this.method = method;
    this.totalAmount = totalAmount;
    this.createdAt = createdAt;
  }

  public static PaymentDetailResponse from(Payment payment) {
    return PaymentDetailResponse.builder()
        .method(payment.getMethod().getMessage())
        .status(payment.getStatus().getMessage())
        .totalAmount(payment.getTotalAmount())
        .createdAt(payment.getCreatedAt())
        .build();
  }
}

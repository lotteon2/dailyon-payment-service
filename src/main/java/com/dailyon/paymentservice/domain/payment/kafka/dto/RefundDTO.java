package com.dailyon.paymentservice.domain.payment.kafka.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDTO {

  private ProductInfo productInfo;
  private Long couponInfoId;
  private PaymentInfo paymentInfo;
  private String orderNo;
  private Long memberId;
  private int refundPoints;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ProductInfo {
    private Long productId;
    private Long sizeId;
    private Long quantity;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaymentInfo {
    private String orderNo;
    private int cancelAmount;
  }
}

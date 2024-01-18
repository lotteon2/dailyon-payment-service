package com.dailyon.paymentservice.domain.payment.kafka.dto;

import com.dailyon.paymentservice.domain.payment.kafka.dto.enums.OrderEvent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

  private List<ProductInfo> productInfos;
  private List<Long> couponInfos;
  private PaymentInfo paymentInfo;
  private String orderNo;
  private Long memberId;
  private int usedPoints;
  private OrderEvent orderEvent;
  private String orderType;
  private String referralCode;

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
    private String pgToken;
  }
}

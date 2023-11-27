package com.dailyon.paymentservice.domain.client.dto;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

public class KakaopayDTO {

  @Getter
  @ToString
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ReadyDTO {
    private String tid;
    private boolean tmsResult;
    private String nextRedirectPcUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectAppUrl;
    private String androidAppScheme;
    private String iosAppScheme;
    private String createdAt;

    @Builder
    private ReadyDTO(
        String tid,
        boolean tmsResult,
        String nextRedirectPcUrl,
        String nextRedirectMobileUrl,
        String nextRedirectAppUrl,
        String androidAppScheme,
        String iosAppScheme,
        String createdAt) {
      this.tid = tid;
      this.tmsResult = tmsResult;
      this.nextRedirectPcUrl = nextRedirectPcUrl;
      this.nextRedirectMobileUrl = nextRedirectMobileUrl;
      this.nextRedirectAppUrl = nextRedirectAppUrl;
      this.androidAppScheme = androidAppScheme;
      this.iosAppScheme = iosAppScheme;
      this.createdAt = createdAt;
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ApproveDTO {
    private String aid;
    private String tid;
    private String cid;

    @JsonProperty("partner_order_id")
    private String orderId;

    @JsonProperty("partner_user_id")
    private String userId;

    @JsonProperty("payment_method_type")
    private String paymentMethod;

    @JsonProperty("item_name")
    private String itemName;

    private Integer quantity;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("approved_at")
    private LocalDateTime approvedAt;

    private Amount amount;

    public CreatePaymentServiceRequest toServiceRequest(PaymentType type, PaymentMethod method) {
      return CreatePaymentServiceRequest.builder()
          .memberId(Long.valueOf(userId))
          .type(type)
          .method(method)
          .totalAmount(amount.getTotal())
          .build();
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CancelDTO {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;

    private Amount amount; // 현재 요청 트랜잭션
    private Amount approvedCancelAmount; // 현재 트랜잭션으로 인한 취소금액
    private Amount canceledAmount; // 누적 취소금액
    private Amount cancelAvailableAmount; // 남은 취소 가능 금액

    private String itemName;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Amount {
    private Integer total;

    @JsonProperty("tax_free")
    private Integer taxFree;

    private Integer vat;

    private Integer point;

    private Integer discount;

    @JsonProperty("green_deposit")
    private Integer greenDeposit;
  }
}

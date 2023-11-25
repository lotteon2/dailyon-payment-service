package com.dailyon.paymentservice.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaopayApproveDTO {
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

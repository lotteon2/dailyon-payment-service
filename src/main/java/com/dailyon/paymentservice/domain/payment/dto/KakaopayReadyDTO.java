package com.dailyon.paymentservice.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaopayReadyDTO {
  private String tid;
  private boolean tmsResult;
  private String nextRedirectPcUrl;
  private String nextRedirectMobileUrl;
  private String nextRedirectAppUrl;
  private String androidAppScheme;
  private String iosAppScheme;
  private String createdAt;

  @Builder
  private KakaopayReadyDTO(
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

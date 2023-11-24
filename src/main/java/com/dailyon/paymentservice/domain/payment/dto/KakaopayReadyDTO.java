package com.dailyon.paymentservice.domain.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
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
}

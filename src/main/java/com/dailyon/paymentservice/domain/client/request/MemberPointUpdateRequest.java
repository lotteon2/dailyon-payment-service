package com.dailyon.paymentservice.domain.client.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPointUpdateRequest {
  private static final String source = "CHARGE";
  private Long amount;

  public MemberPointUpdateRequest(Long amount) {
    this.amount = amount;
  }
}

package com.dailyon.paymentservice.domain.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPointUpdateDTO {
  private final String source = "CHARGE";
  private Long amount;

  @Builder
  private MemberPointUpdateDTO(Long amount) {
    this.amount = amount;
  }
}

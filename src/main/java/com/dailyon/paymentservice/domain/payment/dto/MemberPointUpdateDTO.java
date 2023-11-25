package com.dailyon.paymentservice.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPointUpdateDTO {
  private static final String source = "CHARGE";
  private Long amount;

  @Builder
  private MemberPointUpdateDTO(Long amount) {
    this.amount = amount;
  }
}

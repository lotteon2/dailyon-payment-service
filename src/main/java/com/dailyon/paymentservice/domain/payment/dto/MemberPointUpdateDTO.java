package com.dailyon.paymentservice.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPointUpdateDTO {
  private static final String source = "CHARGE";
  private Long amount;

  public MemberPointUpdateDTO(Long amount) {
    this.amount = amount;
  }
}

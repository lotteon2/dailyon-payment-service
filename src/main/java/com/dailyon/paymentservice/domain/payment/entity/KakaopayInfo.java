package com.dailyon.paymentservice.domain.payment.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class KakaopayInfo {

  @Id private Long paymentId;

  @NotNull private String tid;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @Builder
  private KakaopayInfo(String tid, Payment payment) {
    this.tid = tid;
    this.payment = payment;
  }
}

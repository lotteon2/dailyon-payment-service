package com.dailyon.paymentservice.domain.refund.entity;

import com.dailyon.paymentservice.domain.common.BaseEntity;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Refund extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  @NotNull private String productName;

  private Integer Long;

  @Column(columnDefinition = "int default 0")
  private int points;

  @Builder
  private Refund(Payment payment, String productName, Integer aLong, int points) {
    this.payment = payment;
    this.productName = productName;
    Long = aLong;
    this.points = points;
  }
}

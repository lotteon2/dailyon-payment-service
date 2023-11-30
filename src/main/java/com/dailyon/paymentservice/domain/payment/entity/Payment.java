package com.dailyon.paymentservice.domain.payment.entity;

import com.dailyon.paymentservice.domain.common.BaseEntity;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentStatus;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(name = "idx_type", columnList = "type"))
public class Payment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Long memberId;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentType type;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentStatus status;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentMethod method;

  @NotNull private Integer totalAmount;

  @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, optional = false)
  private OrderPaymentInfo orderPaymentInfo;

  @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY, optional = false)
  private KakaopayInfo kakaopayInfo;

  @Builder
  private Payment(
      Long memberId,
      PaymentType type,
      PaymentStatus status,
      PaymentMethod method,
      Integer totalAmount) {
    this.memberId = memberId;
    this.type = type;
    this.status = status;
    this.method = method;
    this.totalAmount = totalAmount;
  }
}

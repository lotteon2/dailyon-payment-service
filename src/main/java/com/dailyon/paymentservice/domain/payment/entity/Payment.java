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
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private Long memberId;

  @NotNull private PaymentType type;

  @NotNull private PaymentStatus status;

  @NotNull private PaymentMethod method;

  @NotNull private Integer totalAmount;

  @OneToOne(mappedBy = "payment")
  private OrderPaymentInfo orderPaymentInfo;

  @OneToMany(mappedBy = "payment")
  private List<KakaopayInfo> kakaopayInfos;

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

package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.dailyon.paymentservice.domain.payment.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<Payment> findByMemberId(
      Pageable pageable, Long memberId, Long paymentId, PaymentType type) {

    List<Payment> fetch =
        queryFactory
            .selectFrom(payment)
            .where(payment.memberId.eq(memberId), ltPaymentId(paymentId), getType(type))
            .limit(pageable.getPageSize() + 1)
            .orderBy(payment.id.desc())
            .fetch();

    boolean hasNext = false;

    if (fetch.size() == pageable.getPageSize() + 1) {
      fetch.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(fetch, pageable, hasNext);
  }

  private BooleanExpression ltPaymentId(Long paymentId) {
    return paymentId == null ? null : payment.id.lt(paymentId);
  }

  private BooleanExpression getType(PaymentType type) {
    return type == null ? null : payment.type.eq(type);
  }
}

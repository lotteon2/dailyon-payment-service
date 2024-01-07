package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dailyon.paymentservice.domain.payment.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Payment> findByMemberId(Pageable pageable, Long memberId, PaymentType type) {
    List<Long> ids =
        queryFactory
            .select(payment.id)
            .from(payment)
            .where(payment.memberId.eq(memberId), eqType(type))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(payment.id.desc())
            .fetch();

    // 비어있으면 아래 query 실행 하지 않고 바로 비어있는 리스트를 반환한다.
    if (CollectionUtils.isEmpty(ids)) {
      return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
    }

    List<Payment> fetch =
        queryFactory
            .selectFrom(payment)
            .where(payment.id.in(ids))
            .orderBy(payment.id.desc())
            .fetch();

    return PageableExecutionUtils.getPage(fetch, pageable, () -> getTotalPageCount(memberId));
  }

  @Override
  public Optional<Payment> findByOrderNo(String orderNo) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(payment)
            .join(payment.kakaopayInfo)
            .fetchJoin()
            .where(payment.orderNo.eq(orderNo))
            .fetchOne());
  }

  private BooleanExpression eqType(PaymentType type) {
    return type == null ? null : payment.type.eq(type);
  }

  private Long getTotalPageCount(Long memberId) {
    return queryFactory
        .select(payment.count())
        .from(payment)
        .where(payment.memberId.eq(memberId))
        .fetchOne();
  }
}

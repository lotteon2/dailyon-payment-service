package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dailyon.paymentservice.domain.payment.entity.QOrderPaymentInfo.orderPaymentInfo;

@RequiredArgsConstructor
public class OrderPaymentInfoRepositoryImpl implements OrderPaymentInfoRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<OrderPaymentInfo> findByOrderIdFetch(String orderId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(orderPaymentInfo)
            .join(orderPaymentInfo.payment)
            .fetchJoin()
            .where(orderPaymentInfo.orderId.eq(orderId))
            .fetchOne());
  }
}

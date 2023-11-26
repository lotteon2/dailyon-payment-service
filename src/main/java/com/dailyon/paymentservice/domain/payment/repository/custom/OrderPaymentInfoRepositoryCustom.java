package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;

import java.util.Optional;

public interface OrderPaymentInfoRepositoryCustom {
  Optional<OrderPaymentInfo> findByOrderIdFetch(String OrderId);
}

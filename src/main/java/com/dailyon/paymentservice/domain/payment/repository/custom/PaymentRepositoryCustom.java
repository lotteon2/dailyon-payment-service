package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PaymentRepositoryCustom {
  Slice<Payment> findByMemberId(Pageable pageable, Long memberId, Long paymentId, PaymentType type);

  Optional<Payment> findByOrderIdFetch(String orderId);
}

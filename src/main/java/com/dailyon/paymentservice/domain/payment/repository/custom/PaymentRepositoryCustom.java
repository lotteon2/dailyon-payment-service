package com.dailyon.paymentservice.domain.payment.repository.custom;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PaymentRepositoryCustom {
  Page<Payment> findByMemberId(Pageable pageable, Long memberId, PaymentType type);

  Optional<Payment> findByOrderNo(String orderNo);
}

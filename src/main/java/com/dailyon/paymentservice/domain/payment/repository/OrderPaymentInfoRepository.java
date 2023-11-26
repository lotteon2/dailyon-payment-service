package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.domain.payment.entity.OrderPaymentInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentInfoRepository
    extends JpaRepository<OrderPaymentInfo, Payment> {}

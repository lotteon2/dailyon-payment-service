package com.dailyon.paymentservice.domain.payment.repository;

import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaopayInfoRepository extends JpaRepository<KakaopayInfo, Long> {}

package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.repository.KakaopayInfoRepository;
import com.dailyon.paymentservice.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentAppender {
  private final PaymentRepository paymentRepository;
  private final KakaopayInfoRepository kakaopayInfoRepository;

  public Long append(Payment payment, String tid) {
    KakaopayInfo kakaopayInfo = KakaopayInfo.builder().tid(tid).payment(payment).build();
    paymentRepository.save(payment);
    kakaopayInfoRepository.save(kakaopayInfo);
    return payment.getId();
  }
}

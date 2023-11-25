package com.dailyon.paymentservice.domain.payment.implement;

import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
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
    PaymentMethod method = payment.getMethod();
    paymentRepository.save(payment);
    switch (method) {
      case KAKAOPAY:
        {
          KakaopayInfo kakaopayInfo = KakaopayInfo.builder().tid(tid).payment(payment).build();
          kakaopayInfoRepository.save(kakaopayInfo);
        }
        break;
    }
    return payment.getId();
  }
}

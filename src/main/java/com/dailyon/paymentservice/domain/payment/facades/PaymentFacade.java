package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final KakaoPayManager kakaoPayManager;

  public String pointPaymentReady(
      Long memberId, PointPaymentRequest.PointPaymentReadyRequest request) {
    String orderId = OrderNoGenerator.generate(memberId);
    KakaopayReadyDTO readyResponse = kakaoPayManager.ready(memberId, orderId, request);
    return readyResponse.getNextRedirectAppUrl();
  }
}

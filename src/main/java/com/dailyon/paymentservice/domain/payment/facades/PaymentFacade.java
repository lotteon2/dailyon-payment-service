package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.domain.client.MemberFeignClient;
import com.dailyon.paymentservice.domain.client.request.MemberPointUpdateRequest;
import com.dailyon.paymentservice.domain.payment.api.request.PaymentReadyRequest;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import com.dailyon.paymentservice.domain.payment.paymanger.kakaopay.response.KakaopayReadyResponseVO;
import com.dailyon.paymentservice.domain.payment.service.PaymentService;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final KakaoPayManager kakaoPayManager;

  public String pointPaymentReady(
      Long memberId, PaymentReadyRequest.PointPaymentReadyRequest request) {
    String orderId = OrderNoGenerator.generate(memberId);
    KakaopayReadyResponseVO readyResponse = kakaoPayManager.ready(memberId, orderId, request);
    return readyResponse.getNextRedirectAppUrl();
  }
}

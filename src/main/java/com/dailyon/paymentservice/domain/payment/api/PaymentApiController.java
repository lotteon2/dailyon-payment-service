package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.domain.payment.api.request.PaymentReadyRequest;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import com.dailyon.paymentservice.domain.payment.service.PaymentService;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import com.dailyon.paymentservice.domain.payment.vo.kakaopay.KakaopayReadyResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentApiController {
  private final KakaoPayManager kakaoPayManager;
  private final PaymentService paymentService;

  // TODO: TEST를 위해 required false로 함. 추후 바꿈
  @PostMapping("/point-payments/ready")
  public ResponseEntity<String> kakaopayReady(
      @RequestHeader(value = "memberId", required = false,defaultValue = "1") Long memberId,
      @RequestBody PaymentReadyRequest.PointPaymentReadyRequest request) {
    String orderId = OrderNoGenerator.generate(memberId);
    KakaopayReadyResponseVO readyResponse = kakaoPayManager.ready(1L, orderId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(readyResponse.getNextRedirectAppUrl());
  }
}

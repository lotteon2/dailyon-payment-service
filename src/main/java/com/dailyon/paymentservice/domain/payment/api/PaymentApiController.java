package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentApiController {
  private final PaymentFacade paymentFacade;
  // TODO: TEST를 위해 required false + defaultValue설정 함 나중에 바꿈
  @PostMapping("/point-payments/ready")
  public ResponseEntity<String> kakaopayReady(
      @RequestHeader(value = "memberId", required = false, defaultValue = "1") Long memberId,
      @Valid @RequestBody PointPaymentRequest.PointPaymentReadyRequest request) {
    String nextUrl = paymentFacade.pointPaymentReady(memberId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(nextUrl);
  }
}

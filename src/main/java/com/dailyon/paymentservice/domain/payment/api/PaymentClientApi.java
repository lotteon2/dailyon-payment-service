package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.payment.api.request.OrderPaymentRequest;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients/payments")
public class PaymentClientApi {
  private final PaymentFacade paymentFacade;

  @PostMapping("/cancel")
  public ResponseEntity<KakaopayDTO.CancelDTO> cancelOrderPayment(
      @RequestHeader(name = "memberId") Long memberId,
      @Valid @RequestBody OrderPaymentRequest.OrderPaymentCancelRequest request) {
    paymentFacade.cancel(memberId, request);
    return ResponseEntity.ok().build();
  }
}

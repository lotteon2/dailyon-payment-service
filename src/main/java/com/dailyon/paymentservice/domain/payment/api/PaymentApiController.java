package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.dailyon.paymentservice.domain.payment.facades.response.OrderPaymentResponse;
import com.dailyon.paymentservice.domain.payment.facades.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentApiController {
  private final PaymentFacade paymentFacade;
  // TODO: TEST를 위해 required false + defaultValue설정 함 나중에 바꿈
  @PostMapping("/ready")
  public ResponseEntity<String> ready(
      @RequestHeader(value = "memberId", required = false, defaultValue = "1") Long memberId,
      @Valid @RequestBody PointPaymentRequest.PointPaymentReadyRequest request) {

    String orderId = OrderNoGenerator.generate(memberId);
    String nextUrl = paymentFacade.paymentReady(request.toFacadeRequest(memberId, orderId));
    return ResponseEntity.status(HttpStatus.CREATED).body(nextUrl);
  }

  @GetMapping("/approve/{orderId}")
  public ResponseEntity<Long> approve(
      @RequestHeader(value = "memberId", defaultValue = "1") Long memberId,
      @PathVariable(name = "orderId") String orderId,
      @Valid PointPaymentRequest.PointPaymentApproveRequest request) {
    Long paymentId = paymentFacade.paymentApprove(request.toFacadeRequest(memberId, orderId, KAKAOPAY));
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentId);
  }

  @GetMapping("")
  public ResponseEntity<PaymentPageResponse> getPayments(
      @RequestHeader(value = "memberId", defaultValue = "1") Long memberId,
      @PageableDefault(size = 8) Pageable pageable,
      @RequestParam(name = "paymentId", required = false) Long paymentId,
      @RequestParam(name = "type", required = false) PaymentType type) {
    return ResponseEntity.ok(paymentFacade.getPayments(pageable, memberId, paymentId, type));
  }

  @GetMapping("/orders/{orderId}")
  public ResponseEntity<OrderPaymentResponse> getOrderPayment(
      @RequestHeader(value = "memberId", defaultValue = "1") Long memberId,
      @PathVariable(name = "orderId") String orderId) {
    return ResponseEntity.ok(paymentFacade.getOrderPayment(orderId, memberId));
  }
}

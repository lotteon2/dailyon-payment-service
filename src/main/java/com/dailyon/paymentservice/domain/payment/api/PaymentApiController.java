package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.dailyon.paymentservice.domain.payment.facades.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentApiController {
  private final PaymentFacade paymentFacade;

  @Value("${success_url}")
  private String SUCCESS_REDIRECT_URL;
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
      @PathVariable(name = "orderId") String orderId,
      @Valid PointPaymentRequest.PointPaymentApproveRequest request,
      HttpServletResponse response)
      throws IOException {
    Long paymentId = paymentFacade.paymentApprove(request.toFacadeRequest(orderId, KAKAOPAY));
    System.out.println(SUCCESS_REDIRECT_URL+orderId);
    response.sendRedirect(SUCCESS_REDIRECT_URL + orderId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("")
  public ResponseEntity<PaymentPageResponse> getPayments(
      @RequestHeader(value = "memberId", defaultValue = "1") Long memberId,
      @PageableDefault(size = 8) Pageable pageable,
      @RequestParam(name = "type", required = false) PaymentType type) {
    return ResponseEntity.ok(paymentFacade.getPayments(pageable, memberId, type));
  }
}

package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.domain.client.MemberFeignClient;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.client.dto.MemberPointUpdateDTO;
import com.dailyon.paymentservice.domain.payment.api.request.OrderPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.dailyon.paymentservice.domain.payment.facades.response.OrderPaymentResponse;
import com.dailyon.paymentservice.domain.payment.facades.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.message.PaymentEventProducer;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import com.dailyon.paymentservice.domain.payment.service.PaymentService;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO : 코드 리팩토링
@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final PaymentService paymentService;
  private final KakaoPayManager kakaoPayManager;
  private final MemberFeignClient memberFeignClient;
  private final PaymentEventProducer paymentEventProducer;

  public String paymentReady(PaymentFacadeRequest.PaymentReadyRequest request) {
    KakaopayDTO.ReadyDTO readyResponse = kakaoPayManager.ready(request);
    return readyResponse.getNextRedirectPcUrl();
  }

  // TODO : 예외처리 해야함. 코드 래픽토링 예정
  @Transactional
  public Long paymentApprove(PaymentFacadeRequest.PaymentApproveRequest request) {
    KakaopayDTO.ApproveDTO approveDTO = kakaoPayManager.approve(request);
    CreatePaymentServiceRequest serviceRequest =
        approveDTO.toServiceRequest(request.getType(), request.getMethod());
    Long paymentId = paymentService.createPayment(serviceRequest, approveDTO.getTid());

    MemberPointUpdateDTO memberPointUpdateDTO =
        MemberPointUpdateDTO.builder()
            .amount(approveDTO.getAmount().getTotal().longValue())
            .build();
    // pointRecharge 실패하게 되면 kakaopay 결제 취소 요청 보내는 로직 결제 취소 때 작성하고 리팩토링
    memberFeignClient.pointCharge(request.getMemberId(), memberPointUpdateDTO);
    return paymentId;
  }

  @Transactional
  public Long OrderPaymentApprove(PaymentFacadeRequest.PaymentApproveRequest request) {
    KakaopayDTO.ApproveDTO approve = kakaoPayManager.approve(request);
    CreatePaymentServiceRequest serviceRequest = request.toServiceRequest();
    Long paymentId =
        paymentService.createOrderPayment(serviceRequest, approve.getOrderId(), approve.getTid());
    paymentEventProducer.paymentApproved(request.getOrderId());
    return paymentId;
  }

  public PaymentPageResponse getPayments(Pageable pageable, Long memberId, PaymentType type) {
    Page<Payment> page = paymentService.getPayments(pageable, memberId, type);
    return PaymentPageResponse.from(page);
  }

  public OrderPaymentResponse getOrderPayment(String orderId, Long memberId) {
    Payment orderPayment = paymentService.getOrderPayment(orderId, memberId);
    return OrderPaymentResponse.from(orderPayment);
  }

  public KakaopayDTO.CancelDTO cancel(
      Long memberId, OrderPaymentRequest.OrderPaymentCancelRequest request) {
    Payment orderPayment = paymentService.getOrderPayment(request.getOrderId(), memberId);
    KakaopayDTO.CancelDTO cancel =
        kakaoPayManager.cancel(
            orderPayment.getOrderPaymentInfo().getOrderId(), request.getCancelAmount(), memberId);
    return cancel;
  }
}

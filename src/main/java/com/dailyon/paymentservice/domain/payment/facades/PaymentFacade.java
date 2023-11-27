package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.domain.client.MemberFeignClient;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.client.dto.MemberPointUpdateDTO;
import com.dailyon.paymentservice.domain.payment.api.request.OrderPaymentRequest;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import com.dailyon.paymentservice.domain.payment.service.PaymentService;
import com.dailyon.paymentservice.domain.payment.service.request.CreatePaymentServiceRequest;
import com.dailyon.paymentservice.domain.payment.service.response.OrderPaymentResponse;
import com.dailyon.paymentservice.domain.payment.service.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.utils.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.POINT;

// TODO : 코드 리팩토링
@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final PaymentService paymentService;
  private final KakaoPayManager kakaoPayManager;
  private final MemberFeignClient memberFeignClient;

  public String pointPaymentReady(
      Long memberId, PointPaymentRequest.PointPaymentReadyRequest request) {
    String orderId = OrderNoGenerator.generate(memberId);
    KakaopayDTO.ReadyDTO readyResponse = kakaoPayManager.ready(memberId, orderId, request);
    return readyResponse.getNextRedirectAppUrl();
  }

  // TODO : 예외처리 해야함. 코드 래픽토링 예정
  @Transactional
  public Long pointPaymentApprove(
      Long memberId, PointPaymentRequest.PointPaymentApproveRequest request) {
    KakaopayDTO.ApproveDTO approveDTO = kakaoPayManager.approve(memberId, request);
    CreatePaymentServiceRequest serviceRequest = approveDTO.toServiceRequest(POINT, KAKAOPAY);
    Long paymentId = paymentService.createPayment(serviceRequest, approveDTO.getTid());

    MemberPointUpdateDTO memberPointUpdateDTO =
        MemberPointUpdateDTO.builder()
            .amount(approveDTO.getAmount().getTotal().longValue())
            .build();

    // pointRecharge 실패하게 되면 kakaopay 결제 취소 요청 보내는 로직 결제 취소 때 작성하고 리팩토링
    memberFeignClient.pointCharge(memberId, memberPointUpdateDTO);
    return paymentId;
  }

  public PaymentPageResponse getPayments(
      Pageable pageable, Long memberId, Long paymentId, PaymentType type) {
    return paymentService.getPayments(pageable, memberId, paymentId, type);
  }

  public OrderPaymentResponse getOrderPayment(String orderId, Long memberId) {
    return paymentService.getOrderPayment(orderId, memberId);
  }

  public void cancel(Long memberId, OrderPaymentRequest.OrderPaymentCancelRequest request) {
    kakaoPayManager.cancel(request);
  }
}

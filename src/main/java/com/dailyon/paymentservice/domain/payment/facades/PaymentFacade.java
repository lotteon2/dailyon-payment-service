package com.dailyon.paymentservice.domain.payment.facades;

import com.dailyon.paymentservice.domain.client.MemberFeignClient;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.client.dto.MemberPointUpdateDTO;
import com.dailyon.paymentservice.domain.payment.entity.KakaopayInfo;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.dailyon.paymentservice.domain.payment.facades.response.PaymentPageResponse;
import com.dailyon.paymentservice.domain.payment.kafka.dto.RefundDTO;
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

    if (PaymentType.POINT.equals(request.getType())) {
      MemberPointUpdateDTO memberPointUpdateDTO =
          MemberPointUpdateDTO.builder()
              .amount(approveDTO.getAmount().getTotal().longValue())
              .build();
      // pointRecharge 실패하게 되면 kakaopay 결제 취소 요청 보내는 로직 결제 취소 때 작성하고 리팩토링
      memberFeignClient.pointCharge(Long.valueOf(approveDTO.getUserId()), memberPointUpdateDTO);
    }
    return paymentId;
  }

  public PaymentPageResponse getPayments(Pageable pageable, Long memberId, PaymentType type) {
    Page<Payment> page = paymentService.getPayments(pageable, memberId, type);
    return PaymentPageResponse.from(page);
  }

  public void cancelPayments(RefundDTO refundDTO) {
    String orderNo = refundDTO.getOrderNo();
    Payment orderPayment = paymentService.getOrderPayment(orderNo);
    KakaopayInfo kakaopayInfo = orderPayment.getKakaopayInfo();
    kakaoPayManager.cancel(kakaopayInfo.getTid(), refundDTO.getPaymentInfo().getCancelAmount());
  }
}

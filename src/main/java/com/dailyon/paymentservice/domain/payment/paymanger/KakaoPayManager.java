package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.client.dto.KakaopayDTO;
import com.dailyon.paymentservice.domain.payment.entity.Payment;
import com.dailyon.paymentservice.domain.payment.exception.ExpiredPaymentTimeException;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.dailyon.paymentservice.domain.payment.implement.PaymentReader;
import com.dailyon.paymentservice.domain.payment.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType.ORDER;

@RequiredArgsConstructor
@Component
@Slf4j
public class KakaoPayManager {

  private final KakaopayFeignClient client;
  private final PaymentReader paymentReader;
  private final RedisRepository redisRepository; // TODO : DynamoDB 세팅되면 바꿈

  @Value("${kakaopay.cid}")
  private String CID;

  @Value("${kakaopay.admin_key}")
  private String KAKAOPAY_ADMIN_KEY;

  @Value("${kakaopay.approval_url}")
  private String APPROVAL_URL;

  @Value("${kakaopay.order_approval_url}")
  private String ORDER_APPROVAL_URL;

  @Value("${kakaopay.fail_url}")
  private String FAIL_URL;

  @Value("${kakaopay.cancel_url}")
  private String CANCEL_URL;

  // TODO : 나중 리팩토링 예정
  public KakaopayDTO.ReadyDTO ready(PaymentFacadeRequest.PaymentReadyRequest request) {
    MultiValueMap data = toPaymentReadyDTO(request);
    KakaopayDTO.ReadyDTO responseDTO = client.ready("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    responseDTO.setMemberId(request.getMemberId());
    redisRepository.saveReadyInfo(request.getOrderId(), responseDTO);
    return responseDTO;
  }

  // TODO : 나중 리팩토링 예정
  public KakaopayDTO.ApproveDTO approve(PaymentFacadeRequest.PaymentApproveRequest request) {

    KakaopayDTO.ReadyDTO readyDTO =
        redisRepository
            .findByOrderId(request.getOrderId())
            .orElseThrow(ExpiredPaymentTimeException::new);
    MultiValueMap data = toPaymentApproveDTO(readyDTO.getTid(), request, readyDTO.getMemberId());
    KakaopayDTO.ApproveDTO responseDTO = client.approve("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    return responseDTO;
  }

  public KakaopayDTO.CancelDTO cancel(String orderId, Integer cancelAmount, Long memberId) {
    Payment payment = paymentReader.readKakao(orderId, memberId);
    MultiValueMap data = toPaymentCancelDTO(payment.getKakaopayInfo().getTid(), cancelAmount);
    KakaopayDTO.CancelDTO responseDTO = client.cancel("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    return responseDTO;
  }

  private MultiValueMap toPaymentCancelDTO(String tid, Integer cancelAmount) {
    MultiValueMap<String, String> cancelDTO = new LinkedMultiValueMap<>();
    cancelDTO.add("cid", CID);
    cancelDTO.add("tid", tid);
    cancelDTO.add("cancel_amount", cancelAmount.toString());
    cancelDTO.add("cancel_tax_free_amount", "0");
    return cancelDTO;
  }

  // TODO : 클래스로 빼서 관리할예정
  private MultiValueMap toPaymentReadyDTO(PaymentFacadeRequest.PaymentReadyRequest request) {
    MultiValueMap<String, String> readyDTOMap = new LinkedMultiValueMap<>();
    readyDTOMap.add("cid", CID);
    readyDTOMap.add("partner_order_id", request.getOrderId());
    readyDTOMap.add("partner_user_id", request.getMemberId().toString());
    readyDTOMap.add("item_name", request.getProductName());
    readyDTOMap.add("quantity", String.valueOf(request.getQuantity()));
    readyDTOMap.add("total_amount", request.getTotalAmount().toString());
    readyDTOMap.add("tax_free_amount", String.valueOf(request.getQuantity()));
    readyDTOMap.add(
        "approval_url",
        ORDER.equals(request.getType())
            ? ORDER_APPROVAL_URL
            : APPROVAL_URL + "/" + request.getOrderId());
    readyDTOMap.add("cancel_url", CANCEL_URL);
    readyDTOMap.add("fail_url", FAIL_URL);
    return readyDTOMap;
  }

  private MultiValueMap toPaymentApproveDTO(
      String tid, PaymentFacadeRequest.PaymentApproveRequest request, Long memberId) {
    MultiValueMap<String, String> approveDTOMap = new LinkedMultiValueMap<>();
    approveDTOMap.add("cid", CID);
    approveDTOMap.add("tid", tid);
    approveDTOMap.add("partner_order_id", request.getOrderId());
    approveDTOMap.add("partner_user_id", memberId.toString());
    approveDTOMap.add("pg_token", request.getPgToken());
    return approveDTOMap;
  }
}

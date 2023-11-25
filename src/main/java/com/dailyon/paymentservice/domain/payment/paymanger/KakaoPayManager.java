package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayApproveDTO;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class KakaoPayManager {

  private final KakaopayFeignClient client;
  private final RedisRepository redisRepository; // TODO : DynamoDB 세팅되면 바꿈

  @Value("${kakaopay.cid}")
  private String CID;

  @Value("${kakaopay.admin_key}")
  private String KAKAOPAY_ADMIN_KEY;

  @Value("${kakaopay.approval_url}")
  private String APPROVAL_URL;

  @Value("${kakaopay.fail_url}")
  private String FAIL_URL;

  @Value("${kakaopay.cancel_url}")
  private String CANCEL_URL;

  // TODO : 나중 리팩토링 예정
  public KakaopayReadyDTO ready(
      Long memberId, String orderId, PointPaymentRequest.PointPaymentReadyRequest request) {
    MultiValueMap data = toPointPaymentReadyDTO(orderId, memberId, request);
    KakaopayReadyDTO responseDTO = client.ready("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    redisRepository.saveReadyInfo(orderId, responseDTO);
    return responseDTO;
  }

  // TODO : 나중 리팩토링 예정
  public KakaopayApproveDTO approve(
      Long memberId, String tid, PointPaymentRequest.PointPaymentApproveRequest request) {
    MultiValueMap data = toPointPaymentApproveDTO(memberId, tid, request);
    KakaopayApproveDTO responseDTO = client.approve("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    return responseDTO;
  }

  private MultiValueMap toPointPaymentReadyDTO(
      String orderId, Long memberId, PointPaymentRequest.PointPaymentReadyRequest request) {
    MultiValueMap<String, String> readyDTOMap = new LinkedMultiValueMap<>();
    readyDTOMap.add("cid", CID);
    readyDTOMap.add("partner_order_id", orderId);
    readyDTOMap.add("partner_user_id", memberId.toString());
    readyDTOMap.add("item_name", PaymentType.POINT.getMessage());
    readyDTOMap.add("quantity", "1");
    readyDTOMap.add("total_amount", request.getTotalAmount().toString());
    readyDTOMap.add("tax_free_amount", "0");
    readyDTOMap.add("approval_url", APPROVAL_URL + "/" + orderId);
    readyDTOMap.add("cancel_url", CANCEL_URL);
    readyDTOMap.add("fail_url", FAIL_URL);
    return readyDTOMap;
  }

  private MultiValueMap toPointPaymentApproveDTO(
      Long memberId, String tid, PointPaymentRequest.PointPaymentApproveRequest request) {
    MultiValueMap<String, String> approveDTOMap = new LinkedMultiValueMap<>();
    approveDTOMap.add("cid", CID);
    approveDTOMap.add("tid", tid);
    approveDTOMap.add("partner_order_id", request.getOrderId());
    approveDTOMap.add("partner_user_id", memberId.toString());
    approveDTOMap.add("pg_token", request.getPgToken());
    return approveDTOMap;
  }
}

package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.payment.api.request.PaymentReadyRequest;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.dto.KakaopayReadyDTO;
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

  public KakaopayReadyDTO ready(
      Long memberId, String orderId, PaymentReadyRequest.PointPaymentReadyRequest request) {
    MultiValueMap data = createPointPaymentRequest(orderId, memberId, request);
    KakaopayReadyDTO responseVO = client.ready("KakaoAK " + KAKAOPAY_ADMIN_KEY, data);
    redisRepository.saveReadyInfo(orderId, responseVO);
    return responseVO;
  }

  private MultiValueMap createPointPaymentRequest(
      String orderId, Long memberId, PaymentReadyRequest.PointPaymentReadyRequest request) {
    MultiValueMap<String, String> readyRequestMap = new LinkedMultiValueMap<>();
    readyRequestMap.add("cid", CID);
    readyRequestMap.add("partner_order_id", orderId);
    readyRequestMap.add("partner_user_id", memberId.toString());
    readyRequestMap.add("item_name", PaymentType.POINT.name());
    readyRequestMap.add("quantity", "1");
    readyRequestMap.add("total_amount", request.getTotalAmount().toString());
    readyRequestMap.add("tax_free_amount", "0");
    readyRequestMap.add("approval_url", APPROVAL_URL + "/" + orderId);
    readyRequestMap.add("cancel_url", CANCEL_URL);
    readyRequestMap.add("fail_url", FAIL_URL);
    return readyRequestMap;
  }
}

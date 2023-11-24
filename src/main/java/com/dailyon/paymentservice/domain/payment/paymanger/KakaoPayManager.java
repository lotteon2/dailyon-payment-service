package com.dailyon.paymentservice.domain.payment.paymanger;

import com.dailyon.paymentservice.domain.payment.api.request.PaymentReadyRequest;
import com.dailyon.paymentservice.domain.payment.client.KakaopayFeignClient;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.paymanger.kakaopay.response.KakaopayReadyResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoPayManager {

  private final KakaopayFeignClient client;
  private final Environment environment;
  private final RedisTemplate<String, KakaopayReadyResponseVO>
      redisTemplate; // TODO : DynamoDB 세팅되면 바꿈

  private String CID;
  private String KAKAOPAY_ADMIN_KEY;
  private String APPROVAL_URL;
  private String FAIL_URL;
  private String CANCEL_URL;

  @PostConstruct
  private void init() {
    CID = environment.getProperty("kakaopay.cid");
    KAKAOPAY_ADMIN_KEY = "KakaoAK " + environment.getProperty("kakaopay.admin_key");
    APPROVAL_URL = environment.getProperty("kakaopay.approval_url");
    FAIL_URL = environment.getProperty("kakaopay.fail_url");
    CANCEL_URL = environment.getProperty("kakaopay.cancel_url");
  }

  public KakaopayReadyResponseVO ready(
      Long memberId, String orderId, PaymentReadyRequest.PointPaymentReadyRequest request) {
    MultiValueMap data = createPointPaymentRequest(orderId, memberId, request);
    KakaopayReadyResponseVO responseVO = client.ready(KAKAOPAY_ADMIN_KEY, data);
    redisTemplate.opsForValue().set(orderId, responseVO, Duration.ofMinutes(5));
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

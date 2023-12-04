package com.dailyon.paymentservice;

import com.dailyon.paymentservice.domain.client.MemberFeignClient;
import com.dailyon.paymentservice.domain.payment.paymanger.KakaoPayManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "kakaopay.cid=testCid",
      "kakaopey.admin_key=testAdminKey",
      "kakaopay.fail_url=testFailUrl",
      "kakaopay.cancel_url=testCancelUrl",
      "success_url=testSuccessUrl"
    })
@SpringBootTest
public abstract class IntegrationTestSupport {
  @MockBean protected MemberFeignClient memberFeignClient;
  @MockBean protected KakaoPayManager kakaoPayManager;
}

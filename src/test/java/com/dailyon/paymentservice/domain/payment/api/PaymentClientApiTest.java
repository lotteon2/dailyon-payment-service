package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.ControllerTestSupport;
import com.dailyon.paymentservice.domain.payment.api.request.OrderPaymentRequest;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentClientApiTest extends ControllerTestSupport {

  @NotNull(message = "결제 수단은 필수 입니다.")
  private PaymentMethod method;
  @NotNull(message = "결제 금액은 필수 입니다.")
  private Integer totalAmount;

  @DisplayName("주문 상세에서 배송전 상태의 주문 상품의 경우 주문 취소할 수 있다.")
  @Test
  void kakaoOrderPaymentCancel() throws Exception {
    // given
    Long memberId = 1L;
    OrderPaymentRequest.OrderPaymentCancelRequest request =
        new OrderPaymentRequest.OrderPaymentCancelRequest("orderId", 10000);

    // when // then
    mockMvc
        .perform(
            post("/clients/payments/cancel")
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("주문 결제 취소 시 주문번호는 필수 값이다.")
  @Test
  void kakaoOrderPaymentCancelWithNullOrderId() throws Exception {
    // given
    Long memberId = 1L;
    OrderPaymentRequest.OrderPaymentCancelRequest request =
        new OrderPaymentRequest.OrderPaymentCancelRequest(null, 10000);

    // when // then
    mockMvc
        .perform(
            post("/clients/payments/cancel")
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.orderId").value("주문번호는 필수 입니다."));
  }

  @DisplayName("주문 결제 취소 시 취소 금액은 필수 값이다.")
  @Test
  void pointPaymentReadyByNullPayMethod() throws Exception {
    // given
    Long memberId = 1L;
    OrderPaymentRequest.OrderPaymentCancelRequest request =
        new OrderPaymentRequest.OrderPaymentCancelRequest("orderId", null);
    // when // then

    mockMvc
        .perform(
            post("/clients/payments/cancel")
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.cancelAmount").value("취소할 금액은 필수 입니다."));
  }
}

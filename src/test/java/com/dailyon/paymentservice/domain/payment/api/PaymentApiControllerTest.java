package com.dailyon.paymentservice.domain.payment.api;

import com.dailyon.paymentservice.ControllerTestSupport;
import com.dailyon.paymentservice.domain.payment.api.request.PointPaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod.KAKAOPAY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentApiControllerTest extends ControllerTestSupport {

  @DisplayName("카카오 결제를 통해 포인트 결제 준비한다.")
  @Test
  void pointPaymentReadyByKakaopay() throws Exception {
    // given
    PointPaymentRequest.PointPaymentReadyRequest request =
        new PointPaymentRequest.PointPaymentReadyRequest(KAKAOPAY, 250000);

    // when // then
    mockMvc
        .perform(
            post("/payments/point-payments/ready")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("카카오 페이 결제 준비 시 결제 수단은 필수 값이다.")
  @Test
  void pointPaymentReadyByNullPayMethod() throws Exception {
    // given
    PointPaymentRequest.PointPaymentReadyRequest request =
        new PointPaymentRequest.PointPaymentReadyRequest(null, 250000);

    // when // then

    mockMvc
        .perform(
            post("/payments/point-payments/ready")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.method").value("결제 수단은 필수 입니다."));
  }

  @DisplayName("카카오 페이 결제 준비 시 결제 수단은 필수 값이다.")
  @Test
  void pointPaymentReadyByNullAmount() throws Exception {
    // given
    PointPaymentRequest.PointPaymentReadyRequest request =
        new PointPaymentRequest.PointPaymentReadyRequest(null, 250000);

    // when // then
    mockMvc
        .perform(
            post("/payments/point-payments/ready")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.method").value("결제 수단은 필수 입니다."));
  }

  @DisplayName("카카오페이 결제로 포인트 결제한다.")
  @Test
  void pointPaymentApprove() throws Exception {
    // given
    PointPaymentRequest.PointPaymentApproveRequest request =
        new PointPaymentRequest.PointPaymentApproveRequest("orderId", "pgToken");

    // when // then
    mockMvc
        .perform(
            post("/payments/point-payments/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("카카오 페이 결제 승인 요청 시 주문 번호는 필수 값이다.")
  @Test
  void pointPaymentApproveWithNoExistOrderId() throws Exception {
    // given
    String noExistOrderId = null;
    PointPaymentRequest.PointPaymentApproveRequest request =
        new PointPaymentRequest.PointPaymentApproveRequest(noExistOrderId, "pgToken");

    // when // then
    mockMvc
        .perform(
            post("/payments/point-payments/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.orderId").value("주문번호는 필수 입니다."));
  }

  @DisplayName("카카오 페이 결제 승인 요청 시 pgToken은 필수 값이다.")
  @Test
  void pointPaymentApproveWithNullPgToken() throws Exception {
    // given
    String orderId = "orderId";
    String noExistPgToken = null;
    PointPaymentRequest.PointPaymentApproveRequest request =
        new PointPaymentRequest.PointPaymentApproveRequest(orderId, noExistPgToken);

    // when // then
    mockMvc
        .perform(
            post("/payments/point-payments/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.pgToken").value("pgToken은 필수 입니다."));
  }
}

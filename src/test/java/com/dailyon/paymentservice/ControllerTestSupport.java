package com.dailyon.paymentservice;

import com.dailyon.paymentservice.domain.payment.api.PaymentApiController;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PaymentApiController.class})
public class ControllerTestSupport {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @MockBean protected PaymentFacade paymentFacade;
}

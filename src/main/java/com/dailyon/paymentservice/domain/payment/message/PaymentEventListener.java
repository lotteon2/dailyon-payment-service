package com.dailyon.paymentservice.domain.payment.message;

import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
  private final ObjectMapper mapper;
  private final PaymentFacade paymentFacade;
  private final PaymentEventProducer paymentEventProducer;

  @KafkaListener(topics = "stock-deduct-success")
  public void doProcess(String message, Acknowledgment ack) {
    log.info("stock-deduct-success");
    try {
      PaymentFacadeRequest.PaymentApproveRequest request =
          mapper.readValue(message, PaymentFacadeRequest.PaymentApproveRequest.class);
      paymentFacade.paymentApprove(request);
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (FeignException e) { // feign 예외도 구분 해야함. kakao feign 에서 터지면 보상, memberFeign에서 터진거면 다른처리
      paymentEventProducer.paymentFail(message);
    } catch (Exception e) {
      paymentEventProducer.paymentFail(message);
    }
  }
}

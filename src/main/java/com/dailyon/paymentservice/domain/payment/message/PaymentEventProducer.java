package com.dailyon.paymentservice.domain.payment.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  
  public void paymentFail(String message) {
    log.info("payment-fail");
    kafkaTemplate.send("cancel-order", message); // TODO OrderDTO로 바꿔야함
  }
}

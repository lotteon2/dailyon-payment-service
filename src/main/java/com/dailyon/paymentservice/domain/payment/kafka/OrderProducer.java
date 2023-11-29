package com.dailyon.paymentservice.domain.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void paymentApproved(String orderId) {
    log.info("payment-approved -> orderId {}", orderId);
    kafkaTemplate.send("payment-approved", orderId);
  }

}

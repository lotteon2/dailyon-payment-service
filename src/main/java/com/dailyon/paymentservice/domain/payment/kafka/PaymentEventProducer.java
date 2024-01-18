package com.dailyon.paymentservice.domain.payment.kafka;

import com.dailyon.paymentservice.domain.payment.kafka.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper mapper;

  public void paymentFail(OrderDTO orderDTO) {
    log.info("payment-fail");
    try {
      String message = mapper.writeValueAsString(orderDTO);
      kafkaTemplate.send("cancel-order", message); // TODO OrderDTO로 바꿔야함
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}

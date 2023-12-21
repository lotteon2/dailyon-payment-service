package com.dailyon.paymentservice.domain.payment.message;

import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.dailyon.paymentservice.domain.payment.message.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
  private final ObjectMapper mapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final PaymentEventProducer paymentEventProducer;

  @KafkaListener(topics = "use-member-points")
  public void doProcess(String message, Acknowledgment ack) {
    log.info("use-member-points");
    OrderDTO orderDTO = null;
    try {
      orderDTO = mapper.readValue(message, OrderDTO.class);
      kafkaTemplate.send("approve-payment", mapper.writeValueAsString(orderDTO));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      paymentEventProducer.paymentFail(message);
    } finally {
      ack.acknowledge();
    }
  }
}

package com.dailyon.paymentservice.domain.payment.kafka;

import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentMethod;
import com.dailyon.paymentservice.domain.payment.entity.enums.PaymentType;
import com.dailyon.paymentservice.domain.payment.facades.PaymentFacade;
import com.dailyon.paymentservice.domain.payment.facades.request.PaymentFacadeRequest;
import com.dailyon.paymentservice.domain.payment.kafka.dto.OrderDTO;
import com.dailyon.paymentservice.domain.payment.kafka.dto.RefundDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.dailyon.paymentservice.domain.payment.kafka.dto.enums.OrderEvent.PAYMENT_FAIL;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
  private final ObjectMapper mapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final PaymentEventProducer paymentEventProducer;
  private final PaymentFacade paymentFacade;

  @KafkaListener(topics = "use-member-points")
  public void doProcess(String message, Acknowledgment ack) {
    log.info("use-member-points");
    OrderDTO orderDTO = null;
    try {
      orderDTO = mapper.readValue(message, OrderDTO.class);
      PaymentFacadeRequest.PaymentApproveRequest request =
          PaymentFacadeRequest.PaymentApproveRequest.builder()
              .pgToken(orderDTO.getPaymentInfo().getPgToken())
              .method(PaymentMethod.KAKAOPAY)
              .type(PaymentType.ORDER)
              .orderId(orderDTO.getOrderNo())
              .quantity(1)
              .build();

      paymentFacade.paymentApprove(request);
      kafkaTemplate.send("approve-payment", mapper.writeValueAsString(orderDTO));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      orderDTO.setOrderEvent(PAYMENT_FAIL);
      paymentEventProducer.paymentFail(orderDTO);
    } finally {
      ack.acknowledge();
    }
  }

  @KafkaListener(topics = "create-refund")
  public void cancel(String message, Acknowledgment ack) {
    log.info("create-refund");
    RefundDTO refundDTO = null;
    try {
      refundDTO = mapper.readValue(message, RefundDTO.class);
      paymentFacade.cancelPayments(refundDTO);
      ack.acknowledge();
    } catch (JsonProcessingException e) {
      log.error("직렬화 실패");
    } catch (Exception e) {
      log.error("환불 처리 중 예외발생 : {} ", e.getMessage());
    }
  }
}

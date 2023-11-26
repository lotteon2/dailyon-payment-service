package com.dailyon.paymentservice.domain.payment.exception;

import com.dailyon.paymentservice.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderPaymentNotFoundException extends CustomException {
  private static final String MESSAGE = "주문 결제 정보가 존재하지 않습니다.";

  public OrderPaymentNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}

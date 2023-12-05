package com.dailyon.paymentservice.domain.payment.exception;

import com.dailyon.paymentservice.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentNotFoundException extends CustomException {
  private static final String MESSAGE = "결제 정보가 존재하지 않습니다.";

  public PaymentNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}

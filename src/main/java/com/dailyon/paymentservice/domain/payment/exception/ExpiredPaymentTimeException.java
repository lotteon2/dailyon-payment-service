package com.dailyon.paymentservice.domain.payment.exception;

import com.dailyon.paymentservice.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpiredPaymentTimeException extends CustomException {
  private static final String MESSAGE = "결제 시간이 만료 되었습니다.";

  public ExpiredPaymentTimeException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.BAD_REQUEST;
  }
}

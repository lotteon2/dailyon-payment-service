package com.dailyon.paymentservice.domain.payment.exception;

import com.dailyon.paymentservice.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthorizationException extends CustomException {
  private static final String MESSAGE = "권한이 없습니다.";

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.FORBIDDEN;
  }
}

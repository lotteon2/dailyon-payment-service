package com.dailyon.paymentservice.domain.payment.facades.response;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PaymentPageResponse {
  private List<PaymentDetailResponse> payments;
  private boolean hasNext;

  public PaymentPageResponse(List<PaymentDetailResponse> payments, boolean hasNext) {
    this.payments = payments;
    this.hasNext = hasNext;
  }

  public static PaymentPageResponse from(Slice<Payment> slice) {
    List<PaymentDetailResponse> payments =
        slice.getContent().stream().map(PaymentDetailResponse::from).collect(Collectors.toList());
    return new PaymentPageResponse(payments, slice.hasNext());
  }
}

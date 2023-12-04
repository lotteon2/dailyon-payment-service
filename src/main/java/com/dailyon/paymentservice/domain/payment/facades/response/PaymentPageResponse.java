package com.dailyon.paymentservice.domain.payment.facades.response;

import com.dailyon.paymentservice.domain.payment.entity.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PaymentPageResponse {
  private List<PaymentDetailResponse> payments;
  private Integer totalPages;
  private Long totalElements;

  public PaymentPageResponse(
      List<PaymentDetailResponse> payments, Integer totalPages, Long totalElements) {
    this.payments = payments;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public static PaymentPageResponse from(Page<Payment> page) {
    List<PaymentDetailResponse> payments =
        page.getContent().stream().map(PaymentDetailResponse::from).collect(Collectors.toList());
    return new PaymentPageResponse(payments, page.getTotalPages(), page.getTotalElements());
  }
}

package com.dailyon.paymentservice.domain.payment.message.dto;

import com.dailyon.paymentservice.domain.payment.message.dto.enums.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private List<ProductInfo> productInfos;
    private List<Long> couponInfos;
    private PaymentInfo paymentInfo;
    private String orderNo;
    private Long memberId;
    private int usedPoints;
    private OrderEvent orderEvent;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductInfo {
        private Long productId;
        private Long sizeId;
        private Long quantity;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentInfo {
        private String pgToken;
    }
}

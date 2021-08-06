package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SellerRequestVO {
    private String sellerName;
    private String oldSellerName;
    private String newSellerName;
}

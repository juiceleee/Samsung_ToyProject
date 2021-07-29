package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BasketRequestVO {
    private String userName;
    private String itemName;
    private Integer itemCnt;
}

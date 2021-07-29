package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemRequestVO {
    private String itemName;
    private Integer itemCnt;
    private String oldName;
    private String newName;
}

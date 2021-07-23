package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResponseVO {
    String resultCode;
    String resultMessage;
    String resultTraceId;
    String[] resultData;
}

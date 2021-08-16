package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class ResponseVO {
    Map<String, String> result;
}
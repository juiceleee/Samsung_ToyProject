package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class ItemResultVO {
    int status;
    HashMap<String, Integer> map;
}

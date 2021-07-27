package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class SearchResponseVO {
    String resultCode;
    HashMap<Integer, Integer> resultData;
}

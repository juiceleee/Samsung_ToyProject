package com.samsung.bixby.mediastreaming.demo.vo;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class SearchResultVO {
    int status;
    HashMap<Integer, Integer> shoppingList;
}

package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;

import java.util.HashMap;
import java.util.List;

public interface ItemCustomRepository {
    HashMap<String, String> itemEntityToMap(List<ItemEntity> items);
    HashMap<String, String> itemEntityToMap(ItemEntity item);
    ResultVO buildSuccessItem(List<ItemEntity> items);
    ResultVO buildSuccessItem(ItemEntity item);
    boolean isItemExist(String itemName);
    boolean isItemNotExist(String itemName);
}

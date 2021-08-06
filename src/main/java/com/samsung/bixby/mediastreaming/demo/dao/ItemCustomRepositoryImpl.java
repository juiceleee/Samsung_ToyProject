package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BasketRepository basketRepository;

    public HashMap<String, String> itemEntityToMap(List<ItemEntity> items){
        HashMap<String, String> map = new HashMap<>();
        for(ItemEntity item: items){
            map.put(item.getItemname(), item.getStock().toString());
        }
        return map;
    }

    public HashMap<String, String> itemEntityToMap(ItemEntity item){
        HashMap<String, String> map = new HashMap<>();
        if(item==null) return map;

        map.put(item.getItemname(), item.getStock().toString());
        return map;
    }

    public ResultVO buildSuccessItem(List<ItemEntity> items){
        return ResultVO.builder()
                .map(itemEntityToMap(items))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public ResultVO buildSuccessItem(ItemEntity item){
        return ResultVO.builder()
                .map(itemEntityToMap(item))
                .status(Constants.VO_SUCCESS)
                .build();
    }



    public boolean isItemExist(String itemName){
        return itemRepository.findByItemname(itemName).isPresent();
    }

    public boolean isItemNotExist(String itemName){
        return !isItemExist(itemName);
    }
}

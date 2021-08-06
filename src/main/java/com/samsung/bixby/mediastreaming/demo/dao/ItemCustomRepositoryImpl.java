package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    @Autowired
    @Lazy
    ItemRepository itemRepository;

    @Autowired
    @Lazy
    BasketRepository basketRepository;

    @Override
    public HashMap<String, String> itemEntityToMap(List<ItemEntity> items){
        HashMap<String, String> map = new HashMap<>();
        for(ItemEntity item: items){
            map.put(item.getItemname(), item.getStock().toString());
        }
        return map;
    }

    @Override
    public HashMap<String, String> itemEntityToMap(ItemEntity item){
        HashMap<String, String> map = new HashMap<>();
        if(item==null) return map;

        map.put(item.getItemname(), item.getStock().toString());
        return map;
    }

    @Override
    public ResultVO buildSuccessItem(List<ItemEntity> items){
        return ResultVO.builder()
                .map(itemEntityToMap(items))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    @Override
    public ResultVO buildSuccessItem(ItemEntity item){
        return ResultVO.builder()
                .map(itemEntityToMap(item))
                .status(Constants.VO_SUCCESS)
                .build();
    }


    @Override
    public boolean isItemExist(String itemName){
        return itemRepository.findByItemname(itemName).isPresent();
    }

    @Override
    public boolean isItemNotExist(String itemName){
        return !isItemExist(itemName);
    }
}

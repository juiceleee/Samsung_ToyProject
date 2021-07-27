package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.vo.AddItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResponseVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class ShoppingDAO {
    private HashMap<String, HashMap<Integer, Integer>> basket = new HashMap<>();
    private HashMap<String, Integer> item = new HashMap<>();


    public boolean isUserId(String userId) {
        return this.basket.containsKey(userId);
    }


    public SearchResultVO getShoppingListById(String userId) {
        return SearchResultVO.builder()
                                .shoppingList(this.basket.get(userId))
                                .build();


    }

    public SearchResultVO addShoppingListById(String userId, Integer itemId, Integer itemCnt) {
        HashMap<Integer, Integer> temp;
        if(!this.validateItem(itemId)) //item not exist
            return null;

        if (this.isUserId(userId)) {
            temp = this.basket.get(userId);
            if(temp.containsKey(itemId))
                temp.put(itemId, temp.get(itemId) + itemCnt);
            else
                temp.put(itemId, itemCnt);
        }
        else {
            temp = new HashMap<Integer, Integer>();
            temp.put(itemId, itemCnt);
        }
        this.basket.put(userId, temp);
        return SearchResultVO.builder()
                .shoppingList(temp)
                .build();
    }

    private boolean validateItem(Integer itemId) {
        return this.item.containsValue(itemId);
    }

    public AddItemResultVO addItemByName(String itemName, Integer itemNum) {
        if(this.isItem(itemName))
            return null;
        else{
            this.item.put(itemName, itemNum);
            return AddItemResultVO.builder().map(this.item).build();
        }
    }

    private boolean isItem(String itemName) {
        return this.item.containsKey(itemName);
    }

    public AddItemResultVO getItemList() {
        return AddItemResultVO.builder()
                                .map(this.item)
                                .build();
    }
}

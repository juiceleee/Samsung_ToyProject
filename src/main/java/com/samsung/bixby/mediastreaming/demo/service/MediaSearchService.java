package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.dao.ShoppingDAO;
import com.samsung.bixby.mediastreaming.demo.vo.AddItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResponseVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaSearchService {
    private ShoppingDAO shoppingDAO;

    @Autowired
    public MediaSearchService(ShoppingDAO shoppingDAO) {
        this.shoppingDAO = shoppingDAO;
    }

    public SearchResultVO findShoppingListById(String userId) {
        if(shoppingDAO.isUserId(userId))
            return shoppingDAO.getShoppingListById(userId);
        else
            return null;
    }

    public SearchResultVO addShoppingListById(String userId, Integer itemId ,Integer itemCnt) {
        return shoppingDAO.addShoppingListById(userId, itemId, itemCnt);
    }

    public AddItemResultVO addItemByName(String itemName, Integer itemNum) {
        return shoppingDAO.addItemByName(itemName, itemNum);
    }

    public AddItemResultVO getItemList() {
        return shoppingDAO.getItemList();
    }
}

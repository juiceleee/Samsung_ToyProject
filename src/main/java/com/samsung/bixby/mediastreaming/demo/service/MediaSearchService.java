package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.dao.ShoppingDAO;
import com.samsung.bixby.mediastreaming.demo.vo.ItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MediaSearchService {
    private ShoppingDAO shoppingDAO;

    @Autowired
    public MediaSearchService(ShoppingDAO shoppingDAO) {
        this.shoppingDAO = shoppingDAO;
    }

    public SearchResultVO findShoppingListById(String userName) {
        return shoppingDAO.getShoppingListById(userName);
    }

    public SearchResultVO addShoppingListById(String userName, String itemName ,Integer itemCnt) {
        return shoppingDAO.addShoppingListById(userName, itemName, itemCnt);
    }

    public ItemResultVO addItemByName(String itemName, Integer itemCnt) {
        return shoppingDAO.addItemByName(itemName, itemCnt);
    }

    public ItemResultVO getItemList() {
        return shoppingDAO.getItemList();
    }

    public UserResultVO getUserList() {
        return shoppingDAO.getUserList();
    }

    public UserResultVO addUser(String userName) {
        return shoppingDAO.addUser(userName);
    }

    public UserResultVO deleteUser(String userName) {
        return shoppingDAO.deleteUser(userName);
    }

    public UserResultVO changeUser(String oldUserName, String newUserName) {
        return shoppingDAO.changeUser(oldUserName, newUserName);
    }
}

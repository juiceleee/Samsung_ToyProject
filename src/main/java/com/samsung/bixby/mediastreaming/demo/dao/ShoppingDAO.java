package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.vo.ItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserResultVO;
import jdk.jpackage.internal.Log;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShoppingDAO {

    private Integer userCnt = 0;
    private Integer itemCnt = 0;

    private static class Pair<T,S>{
        T first;
        S second;

        public Pair(T first, S second){
            this.first = first;
            this.second = second;
        }
    }
    //user : userName, userId
    private HashMap<String, Integer> userDB = new HashMap<>();
    //basket : userId, itemId, itemCnt
    private HashMap<Integer, HashMap<Integer, Integer>> basketDB = new HashMap<>();
    //itemId : itemName, itemId
    private HashMap<String, Integer> itemIdDB = new HashMap<>();
    //itemCnt : itemId, itemCnt
    private HashMap<Integer, Integer> itemCntDB = new HashMap<>();


    /** /user methods **/
    public UserResultVO getUserList() {
        return UserResultVO.builder()
                            .map(userDB)
                            .build();
    }

    public UserResultVO addUser(String userName) {
        if(this.userDB.containsKey(userName))
            return null;
        else {
            this.userDB.put(userName, this.userCnt);
            this.basketDB.put(this.userCnt, new HashMap<>()); //add empty basket
            this.userCnt += 1;
            return UserResultVO.builder()
                                .map(this.userDB)
                                .build();
        }

    }

    public boolean isUserName(String userName) {
        return this.userDB.containsKey(userName);
    }

    public Integer getUserIdByName(String userName){
        return this.userDB.get(userName);
    }


    /** /list methods **/

    public SearchResultVO getShoppingListById(String userName) {
        if(!isUserName(userName))
            return SearchResultVO.builder()
                                    .shoppingList(null)
                                    .build();
        else {
            return SearchResultVO.builder()
                    .shoppingList(this.basketDB.get(this.getUserIdByName(userName)))
                    .build();
        }


    }

    public SearchResultVO addShoppingListById(String userName, String itemName, Integer itemCnt) {
        if(!this.isItem(itemName)) //item not exist
            return null;
        if(!this.isUserName(userName)) //user not exist
            return null;

        HashMap<Integer, Integer> temp;
        Integer userId = this.getUserIdByName(userName);
        Integer itemId = this.getItemIdByName(itemName);


        if (this.getShoppingListById(userName).getShoppingList() != null) {
            temp = this.basketDB.get(userId);
            if(temp.containsKey(itemId))
                temp.put(itemId, temp.get(itemId) + itemCnt);
            else
                temp.put(itemId, itemCnt);
        }
        else {
            temp = new HashMap<>();
            temp.put(itemId, itemCnt);
        }
        this.basketDB.put(userId, temp);
        return this.getShoppingListById(userName);
    }

    /** /item methods **/

    private boolean isItem(String itemName) {
        return this.itemIdDB.containsKey(itemName);
    }

    private Integer getItemIdByName(String itemName){
        return this.itemIdDB.get(itemName);
    }


    public ItemResultVO addItemByName(String itemName, Integer itemCnt) {
        if(this.isItem(itemName)){
            Integer itemId = this.getItemIdByName(itemName);
            this.itemCntDB.put(itemId, itemCntDB.get(itemId) + itemCnt);
        }
        else{
            itemIdDB.put(itemName, this.itemCnt);
            itemCntDB.put(this.itemCnt, itemCnt);
            this.itemCnt += 1;
        }

        return this.getItemList();

    }

    public ItemResultVO getItemList() {
        HashMap<String, Integer> temp = new HashMap<>();
        for(HashMap.Entry<String, Integer> entry : this.itemIdDB.entrySet()){
            temp.put(entry.getKey(), this.itemCntDB.get(entry.getValue()));
        }

        return ItemResultVO.builder()
                .map(temp)
                .build();
    }
}

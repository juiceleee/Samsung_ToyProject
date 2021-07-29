package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.mockDB.DB;
import com.samsung.bixby.mediastreaming.demo.vo.ItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.BasketResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserResultVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ShoppingDAO {
    private DB db = new DB();

    public HashMap<String, String> changeMapToString(HashMap<?, ?> map){
        HashMap<String, String> result = new HashMap<>();
        for(HashMap.Entry<?, ?> entry : map.entrySet()){
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }


    /** /user methods **/
    public UserResultVO getUserList() {
        return UserResultVO.builder()
                            .map(this.changeMapToString(db.userDB))
                            .status(Constants.VO_SUCCESS)
                            .build();
    }

    public UserResultVO addUser(String userName) {
        if(db.userDB.containsKey(userName))
            return UserResultVO.builder()
                                .map(null)
                                .status(Constants.VO_USER_ALREADY_EXIST)
                                .build();
        else {
            db.userDB.put(userName, db.userCnt);
            db.basketDB.put(db.userCnt, new HashMap<>()); //add empty basket
            db.userCnt += 1;
            return getUserList();
        }

    }

    public UserResultVO deleteUser(String userName) {
        if(!db.userDB.containsKey(userName))
            return UserResultVO.builder()
                                .map(null)
                                .status(Constants.VO_USER_NOT_EXIST)
                                .build();
        else{
            Integer userId = this.getUserIdByName(userName);
            db.basketDB.remove(userId);
            db.userDB.remove(userName);
            return getUserList();
        }
    }

    public UserResultVO changeUser(String oldUserName, String newUserName) {
        if(!db.userDB.containsKey(oldUserName))
            return UserResultVO.builder()
                                .map(null)
                                .status(Constants.VO_USER_NOT_EXIST)
                                .build();

        if(db.userDB.containsKey(newUserName))
            return UserResultVO.builder()
                    .map(null)
                    .status(Constants.VO_USER_ALREADY_EXIST)
                    .build();

        Integer userId = this.getUserIdByName(oldUserName);
        db.userDB.remove(oldUserName);
        db.userDB.put(newUserName, userId);
        return getUserList();
    }

    public boolean isUserName(String userName) {
        return db.userDB.containsKey(userName);
    }

    public Integer getUserIdByName(String userName){
        return db.userDB.get(userName);
    }

    /** /basket methods **/

    public BasketResultVO getShoppingListById(String userName) {
        if(!isUserName(userName))
            return BasketResultVO.builder()
                                    .shoppingList(null)
                                    .status(Constants.VO_USER_NOT_EXIST)
                                    .build();
        else {
            return BasketResultVO.builder()
                    .shoppingList(this.changeMapToString(db.basketDB.get(this.getUserIdByName(userName))))
                    .status(Constants.VO_SUCCESS)
                    .build();
        }


    }

    public BasketResultVO addShoppingListById(String userName, String itemName, Integer itemCnt) {
        if(!this.isItem(itemName)) //item not exist
            return BasketResultVO.builder()
                                    .shoppingList(null)
                                    .status(Constants.VO_ITEM_NOT_EXIST)
                                    .build();
        if(!this.isUserName(userName)) //user not exist
            return BasketResultVO.builder()
                                    .shoppingList(null)
                                    .status(Constants.VO_USER_NOT_EXIST)
                                    .build();

        HashMap<Integer, Integer> temp;
        Integer userId = this.getUserIdByName(userName);
        Integer itemId = this.getItemIdByName(itemName);


        if (this.getShoppingListById(userName).getShoppingList() != null) {
            temp = db.basketDB.get(userId);
            if(temp.containsKey(itemId))
                temp.put(itemId, temp.get(itemId) + itemCnt);
            else
                temp.put(itemId, itemCnt);
        }
        else {
            temp = new HashMap<>();
            temp.put(itemId, itemCnt);
        }
        db.basketDB.put(userId, temp);
        return this.getShoppingListById(userName);
    }

    public BasketResultVO deleteItemFromShoppingList(String userName, String itemName, Integer itemCnt) {
        if(!this.isItem(itemName)) //item not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();
        if(!this.isUserName(userName)) //user not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_USER_NOT_EXIST)
                    .build();

        Integer itemId = this.getItemIdByName(itemName);
        Integer userId = this.getUserIdByName(userName);

        if(!db.basketDB.get(userId).containsKey(itemId)) // item not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();

        Integer curItemCnt = db.basketDB.get(userId).get(itemId);
        if(curItemCnt < itemCnt)
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_ITEM_CNT_TOO_MUCH)
                    .build();

        db.basketDB.get(userId).remove(itemId);

        if(!(curItemCnt == itemCnt))
            db.basketDB.get(userId).put(itemId, curItemCnt - itemCnt);

        return this.getShoppingListById(userName);
    }

    public BasketResultVO deleteItemFromShoppingList(String userName, String itemName) {
        if(!this.isItem(itemName)) //item not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();
        if(!this.isUserName(userName)) //user not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_USER_NOT_EXIST)
                    .build();

        Integer itemId = this.getItemIdByName(itemName);
        Integer userId = this.getUserIdByName(userName);

        if(!db.basketDB.get(userId).containsKey(itemId)) // item not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();

        db.basketDB.get(userId).remove(itemId);
        return this.getShoppingListById(userName);
    }

    public BasketResultVO deleteItemFromShoppingList(String userName) {
        if(!this.isUserName(userName)) //user not exist
            return BasketResultVO.builder()
                    .shoppingList(null)
                    .status(Constants.VO_USER_NOT_EXIST)
                    .build();

        Integer userId = this.getUserIdByName(userName);

        db.basketDB.remove(userId);
        db.basketDB.put(userId, new HashMap<>());
        return this.getShoppingListById(userName);
    }

    /** /item methods **/

    private boolean isItem(String itemName) {
        return db.itemIdDB.containsKey(itemName);
    }

    private Integer getItemIdByName(String itemName){
        return db.itemIdDB.get(itemName);
    }

    private Integer getItemCntById(Integer itemId){ return db.itemCntDB.get(itemId); }


    public ItemResultVO addItemByName(String itemName, Integer itemCnt) {
        if(this.isItem(itemName)){
            Integer itemId = this.getItemIdByName(itemName);
            db.itemCntDB.put(itemId, db.itemCntDB.get(itemId) + itemCnt);
        }
        else{
            db.itemIdDB.put(itemName, db.itemCnt);
            db.itemCntDB.put(db.itemCnt, itemCnt);
            db.itemCnt += 1;
        }

        return this.getItemList();

    }

    public ItemResultVO getItemList() {
        HashMap<String, Integer> temp = new HashMap<>();
        for(HashMap.Entry<String, Integer> entry : db.itemIdDB.entrySet()){
            temp.put(entry.getKey(), db.itemCntDB.get(entry.getValue()));
        }

        return ItemResultVO.builder()
                .status(Constants.VO_SUCCESS)
                .map(changeMapToString(temp))
                .build();
    }

    //TODO : Check item in user basket overflows
    public ItemResultVO deleteItem(String itemName, Integer itemCnt){
        if(!this.isItem(itemName))
            return ItemResultVO.builder()
                                .map(null)
                                .status(Constants.VO_ITEM_NOT_EXIST)
                                .build();

        Integer itemId = this.getItemIdByName(itemName);
        Integer curItemCnt = this.getItemCntById(itemId);

        if(curItemCnt < itemCnt)
            return ItemResultVO.builder()
                                .map(null)
                                .status(Constants.VO_ITEM_CNT_TOO_MUCH)
                                .build();

        db.itemCntDB.remove(itemId);

        if(!(curItemCnt == itemCnt))
            db.itemCntDB.put(itemId, curItemCnt - itemCnt);

        return getItemList();
    }

    public ItemResultVO deleteItem(String itemName){
        if(!this.isItem(itemName))
            return ItemResultVO.builder()
                    .map(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();

        Integer itemId = this.getItemIdByName(itemName);
        db.itemCntDB.remove(itemId);

        return getItemList();
    }


    public ItemResultVO changeItemName(String oldName, String newName) {
        if(!this.isItem(oldName))
            return ItemResultVO.builder()
                    .map(null)
                    .status(Constants.VO_ITEM_NOT_EXIST)
                    .build();
        if(this.isItem(newName))
            return ItemResultVO.builder()
                    .map(null)
                    .status(Constants.VO_ITEM_ALREADY_EXIST)
                    .build();

        Integer itemId = this.getItemIdByName(oldName);
        db.itemIdDB.remove(oldName);
        db.itemIdDB.put(newName, itemId);

        return getItemList();
    }
}

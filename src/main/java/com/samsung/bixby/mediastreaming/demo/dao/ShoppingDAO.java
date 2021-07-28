package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.mockDB.DB;
import com.samsung.bixby.mediastreaming.demo.vo.ItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserResultVO;
import jdk.jpackage.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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


    /** /list methods **/

    public SearchResultVO getShoppingListById(String userName) {
        if(!isUserName(userName))
            return SearchResultVO.builder()
                                    .shoppingList(null)
                                    .status(Constants.VO_USER_NOT_EXIST)
                                    .build();
        else {
            return SearchResultVO.builder()
                    .shoppingList(this.changeMapToString(db.basketDB.get(this.getUserIdByName(userName))))
                    .status(Constants.VO_SUCCESS)
                    .build();
        }


    }

    public SearchResultVO addShoppingListById(String userName, String itemName, Integer itemCnt) {
        if(!this.isItem(itemName)) //item not exist
            return SearchResultVO.builder()
                                    .shoppingList(null)
                                    .status(Constants.VO_ITEM_NOT_EXIST)
                                    .build();
        if(!this.isUserName(userName)) //user not exist
            return SearchResultVO.builder()
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

    /** /item methods **/

    private boolean isItem(String itemName) {
        return db.itemIdDB.containsKey(itemName);
    }

    private Integer getItemIdByName(String itemName){
        return db.itemIdDB.get(itemName);
    }


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
}

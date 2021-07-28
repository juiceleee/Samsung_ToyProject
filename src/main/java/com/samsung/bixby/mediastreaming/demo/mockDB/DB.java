package com.samsung.bixby.mediastreaming.demo.mockDB;

import java.util.HashMap;

public class DB {
    public Integer userCnt = 0;
    public Integer itemCnt = 0;

    //user : userName, userId
    public HashMap<String, Integer> userDB = new HashMap<>();
    //basket : userId, itemId, itemCnt
    public HashMap<Integer, HashMap<Integer, Integer>> basketDB = new HashMap<>();
    //itemId : itemName, itemId
    public HashMap<String, Integer> itemIdDB = new HashMap<>();
    //itemCnt : itemId, itemCnt
    public HashMap<Integer, Integer> itemCntDB = new HashMap<>();
}

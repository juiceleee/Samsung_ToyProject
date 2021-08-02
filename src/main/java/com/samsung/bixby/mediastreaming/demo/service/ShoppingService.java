package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.*;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ShoppingService {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BasketRepository basketRepository;

    @Autowired
    public ShoppingService(UserRepository userRepository,
                           ItemRepository itemRepository,
                           BasketRepository basketRepository){
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.basketRepository = basketRepository;
    }

    private ResultVO buildSuccessUser(List<UserEntity> users){
        return ResultVO.builder()
                .map(userEntityToMap(users))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    private ResultVO buildError(Integer status){
        return ResultVO.builder()
                .map(null)
                .status(status)
                .build();
    }

    private boolean isUserNotExist(String userName){
        return !isUserExist(userName);
    }

    private boolean isUserExist(String userName){
        return userRepository.findByUsername(userName).isPresent();
    }

    private HashMap<String, String> userEntityToMap(List<UserEntity> users){
        HashMap<String, String> map = new HashMap<>();
        for(UserEntity user: users){
            map.put(user.getUsername(), user.getUserid().toString());
        }
        return map;
    }

    public ResultVO getUserList() {
        List<UserEntity> users = userRepository.findAll();
        return buildSuccessUser(users);
}

    public ResultVO addUser(String userName){
        if(isUserExist(userName))
            return buildError(Constants.VO_USER_ALREADY_EXIST);

        userRepository.save(UserEntity.builder().username(userName).build());
        return getUserList();
    }

    public ResultVO deleteUser(String userName){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        userRepository.deleteByUsername(userName);
        return getUserList();
    }

    public ResultVO changeUser(String oldName, String newName){
        if(isUserNotExist(oldName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(isUserExist(newName))
            return buildError(Constants.VO_USER_ALREADY_EXIST);

        userRepository.updateUserName(oldName, newName);
        return getUserList();
    }


    private HashMap<String, String> itemEntityToMap(List<ItemEntity> items){
        HashMap<String, String> map = new HashMap<>();
        for(ItemEntity item: items){
            map.put(item.getItemname(), item.getItemcnt().toString());
        }
        return map;
    }

    private ResultVO buildSuccessItem(List<ItemEntity> items){
        return ResultVO.builder()
                        .map(itemEntityToMap(items))
                        .status(Constants.VO_SUCCESS)
                        .build();
    }

    private boolean isItemExist(String itemName){
        return itemRepository.findByItemname(itemName).isPresent();
    }

    private boolean isItemNotExist(String itemName){
        return !isItemExist(itemName);
    }

    private Integer getItemIdByName(String itemName){
        assert(itemRepository.findByItemname(itemName).isPresent());

        return itemRepository.findByItemname(itemName).get().getItemid();
    }

    private Integer getItemCntByName(String itemName){
        assert(itemRepository.findByItemname(itemName).isPresent());

        return itemRepository.findByItemname(itemName).get().getItemcnt();
    }


    public ResultVO getItemList(){
        List<ItemEntity> items = itemRepository.findAll();
        return buildSuccessItem(items);
    }

    public ResultVO addItemByName(String itemName, Integer itemCnt){
        if(isItemExist(itemName))
            return buildError(Constants.VO_ITEM_ALREADY_EXIST);

        itemRepository.save(ItemEntity.builder()
                                        .itemname(itemName)
                                        .itemcnt(itemCnt)
                                        .build());

        return getItemList();
    }

    public ResultVO deleteItem(String itemName, Integer itemCnt){
        if(isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer itemId = getItemIdByName(itemName);
        Integer curItemCnt = getItemCntByName(itemName);

        if(curItemCnt < itemCnt)
            return buildError(Constants.VO_ITEM_CNT_TOO_MUCH);

        itemRepository.deleteItemCnt(curItemCnt - itemCnt, itemId);
        return getItemList();
    }

    public ResultVO deleteItem(String itemName){
        if(isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer itemId = getItemIdByName(itemName);

        itemRepository.deleteById(itemId);
        return getItemList();
    }

    public ResultVO changeItemName(String oldName, String newName){
        if(isItemNotExist(oldName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        if(isItemExist(newName))
            return buildError(Constants.VO_ITEM_ALREADY_EXIST);

        itemRepository.updateItemName(oldName, newName);
        return getItemList();
    }

    private HashMap<String, String> basketEntityToMap(List<BasketEntity> userBasket){
        HashMap<String, String> map = new HashMap<>();
        for(BasketEntity basket: userBasket){

            map.put(itemRepository.findById(basket.getItemid()).get().getItemname()
                    ,basket.getItemcnt().toString());
        }
        return map;
    }

    private ResultVO buildSuccessBasket(List<BasketEntity> basket){
        return ResultVO.builder()
                .map(basketEntityToMap(basket))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public ResultVO getShoppingListById(String userName){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        Integer userId = userRepository.findByUsername(userName).get().getUserid();
        List<BasketEntity> basket = basketRepository.findByUserid(userId);

        return buildSuccessBasket(basket);
    }

    public ResultVO addShoppingListById(String userName, String itemName, Integer itemCnt){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer userId = userRepository.findByUsername(userName).get().getUserid();
        Integer itemId = getItemIdByName(itemName);

        List<BasketEntity> items = basketRepository.findByUseridAndItemid(userId, itemId);
        if(items.isEmpty())
            basketRepository.save(BasketEntity.builder()
                                                .userid(userId)
                                                .itemid(itemId)
                                                .itemcnt(itemCnt)
                                                .build());
        else{
            Integer curItemCnt = items.get(0).getItemcnt();
            basketRepository.updateItemCnt(curItemCnt+itemCnt, userId, itemId);
        }


        return getShoppingListById(userName);
    }

    public ResultVO deleteItemFromShoppingList(String userName, String itemName, Integer itemCnt){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer userId = userRepository.findByUsername(userName).get().getUserid();
        Integer itemId = getItemIdByName(itemName);

        List<BasketEntity> items = basketRepository.findByUseridAndItemid(userId, itemId);
        if(items.isEmpty())
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        else{
            Integer curItemCnt = items.get(0).getItemcnt();
            basketRepository.updateItemCnt(curItemCnt-itemCnt, userId, itemId);
        }
        return getShoppingListById(userName);
    }

    public ResultVO deleteItemFromShoppingList(String userName, String itemName){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer userId = userRepository.findByUsername(userName).get().getUserid();
        Integer itemId = getItemIdByName(itemName);

        List<BasketEntity> items = basketRepository.findByUseridAndItemid(userId, itemId);
        if(items.isEmpty())
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        else
            basketRepository.deleteByItemidAndUserid(itemId, userId);

        return getShoppingListById(userName);
    }

    public ResultVO deleteItemFromShoppingList(String userName){
        if(isUserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        Integer userId = userRepository.findByUsername(userName).get().getUserid();

        basketRepository.deleteByUserid(userId);

        return getShoppingListById(userName);
    }
}

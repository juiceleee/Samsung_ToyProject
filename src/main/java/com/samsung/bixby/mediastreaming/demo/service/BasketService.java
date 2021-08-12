package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.*;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.samsung.bixby.mediastreaming.demo.service.helper.Helper.buildError;

@Service
public class BasketService {
    private BasketRepository basketRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private EntityManager entityManager;

    @Autowired
    public BasketService(BasketRepository basketRepository,
                         UserRepository userRepository,
                         ItemRepository itemRepository,
                         EntityManager entityManager){
        this.basketRepository = basketRepository;
        this.userRepository =  userRepository;
        this.itemRepository = itemRepository;
        this.entityManager = entityManager;
    }

    public ResultVO getShoppingListById(String userName){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        List<BasketEntity> basket = basketRepository.findByUserAndIsbought(user, false);

        return basketRepository.buildSuccessBasket(basket);
    }

    @Transactional
    public ResultVO addShoppingListById(String userName, String itemName, Integer itemCnt){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        ItemEntity item = itemRepository.findByItemname(itemName).get();
        BasketEntity basket;

        List<BasketEntity> items = basketRepository.findByUserAndItemAndIsbought(user, item, false);
        if(items.isEmpty()) {
            basket = basketRepository.save(BasketEntity.builder()
                    .user(user)
                    .item(item)
                    .itemcnt(itemCnt)
                    .isbought(false)
                    .build());

            user.addBasket(basket);
            entityManager.flush();

        }
        else{
            Integer curItemCnt = items.get(0).getItemcnt();
            basketRepository.updateItemCnt(curItemCnt+itemCnt, user, item);
            basket = null;
        }

        return basketRepository.buildSuccessBasket(basket);
    }

    @Transactional
    public ResultVO confirmBuying(String userName, String itemName){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        ItemEntity item = itemRepository.findByItemname(itemName).get();
        Integer stock = item.getStock();

        List<BasketEntity> items = basketRepository.findByUserAndItemAndIsbought(user, item, false);

        if(items.isEmpty())
            return buildError(Constants.VO_ITEM_NOT_IN_BASKET);

        Integer itemCnt = items.get(0).getItemcnt();

        if(stock < itemCnt)
            return buildError(Constants.VO_ITEM_OUT_OF_STOCK);
        else
            itemRepository.updateItemStock(stock-itemCnt, item.getItemid());


        basketRepository.updateItemStatus(items.get(0).getRownum());


        return basketRepository.buildSuccessBasket((BasketEntity) null);
    }


    @Transactional
    public ResultVO updateItemFromShoppingList(String userName, String itemName, Integer itemCnt){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        ItemEntity item = itemRepository.findByItemname(itemName).get();

        List<BasketEntity> items = basketRepository.findByUserAndItemAndIsbought(user, item, false);
        if(items.isEmpty())
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        Integer curItemCnt = items.get(0).getItemcnt();
        if(itemCnt > 0) {
            //adding item
            basketRepository.updateItemCnt(curItemCnt+itemCnt, user, item);
            return basketRepository.buildSuccessBasket((BasketEntity) null);
        }
        else{ //deleting item
            if (curItemCnt + itemCnt < 0)
                return buildError(Constants.VO_ITEM_CNT_TOO_MUCH);
            else {
                if (curItemCnt + itemCnt == 0)
                    return deleteItemFromShoppingList(userName, itemName);
                else {
                    basketRepository.updateItemCnt(curItemCnt + itemCnt, user, item);
                    return basketRepository.buildSuccessBasket((BasketEntity) null);
                }
            }
        }
    }

    @Transactional
    public ResultVO deleteItemFromShoppingList(String userName, String itemName){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        ItemEntity item = itemRepository.findByItemname(itemName).get();

        List<BasketEntity> baskets = basketRepository.findByUserAndItemAndIsbought(user, item, false);
        if(baskets.isEmpty())
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        else {
            user.removeBasket(baskets.get(0));
            entityManager.flush();
            basketRepository.deleteByUserAndItemAndIsbought(user, item, false);
        }

        return basketRepository.buildSuccessBasket((BasketEntity) null);
    }

    @Transactional
    public ResultVO deleteItemFromShoppingList(String userName){
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();

        List<BasketEntity> baskets = user.getBaskets();

        for(BasketEntity basket : baskets){
            user.removeBasket(basket);
            entityManager.flush();
        }

        basketRepository.deleteByUser(user);

        return basketRepository.buildSuccessBasket((BasketEntity) null);
    }

    public ResultVO getBoughtItem(String userName) {
        if(userRepository.UserNotExist(userName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        UserEntity user = userRepository.findByUsername(userName).get();
        List<BasketEntity> baskets = basketRepository.findByUserAndIsbought(user, true);

        return basketRepository.buildSuccessBasket(baskets);

    }
}

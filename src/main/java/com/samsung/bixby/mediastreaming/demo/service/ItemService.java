package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.BasketRepository;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.dao.ItemRepository;
import com.samsung.bixby.mediastreaming.demo.dao.SellerRepository;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.samsung.bixby.mediastreaming.demo.service.helper.Helper.buildError;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private SellerRepository sellerRepository;
    private EntityManager entityManager;
    private BasketRepository basketRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       SellerRepository sellerRepository,
                       BasketRepository basketRepository,
                       EntityManager entityManager){
        this.itemRepository = itemRepository;
        this.sellerRepository = sellerRepository;
        this.entityManager = entityManager;
        this.basketRepository = basketRepository;
    }

    public ResultVO getItemList(){
        return itemRepository.buildSuccessItem(itemRepository.findAll());
    }


    public ResultVO getItemListBySellerName(String sellerName){
        if(sellerRepository.SellerNotExist(sellerName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        return itemRepository.buildSuccessItem(sellerRepository.findByName(sellerName).get().getItems());
    }


    public ResultVO getItemListByKeyword(String keyword){
        return itemRepository.buildSuccessItem(itemRepository.findByItemnameContaining(keyword));
    }

    @Transactional
    public ResultVO addItemByName(String sellerName, String itemName, Integer itemCnt){
        if(itemRepository.isItemExist(itemName))
            return buildError(Constants.VO_ITEM_ALREADY_EXIST);
        if(sellerRepository.SellerNotExist(sellerName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        SellerEntity seller = sellerRepository.findByName(sellerName).get();
        ItemEntity item = itemRepository.save(ItemEntity.builder()
                .itemname(itemName)
                .stock(itemCnt)
                .seller(seller)
                .build());

        seller.addItem(item);
        entityManager.flush();
        return itemRepository.buildSuccessItem(item);
    }

    public ResultVO updateItem(String sellerName, String itemName, Integer itemStock){
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        if(sellerRepository.SellerNotExist(sellerName))
            return buildError(Constants.VO_USER_NOT_EXIST);

        ItemEntity item = itemRepository.findByItemname(itemName).get();
        if(!item.getSeller().equals(sellerRepository.findByName(sellerName).get()))
            return buildError(Constants.VO_ITEM_OWNER_NOT_SAME);
        Integer itemId = item.getItemid();
        Integer curItemStock = item.getStock();

        if(itemStock > 0){
            //adding item
            itemRepository.updateItemStock(itemStock+curItemStock, itemId);
            return itemRepository.buildSuccessItem((ItemEntity) null);
        }
        else{ //deleting item
            if(curItemStock + itemStock > 0){
                itemRepository.updateItemStock(curItemStock + itemStock, itemId);
                return itemRepository.buildSuccessItem((ItemEntity) null);
            }
            else if(curItemStock + itemStock == 0)
                return deleteItem(itemName, sellerName);
            else
                return buildError(Constants.VO_ITEM_CNT_TOO_MUCH);
        }
    }

    public ResultVO deleteItem(String itemName, String sellerName){
        if(sellerRepository.SellerNotExist(sellerName))
            return buildError(Constants.VO_SELLER_NOT_EXIST);
        if(itemRepository.isItemNotExist(itemName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);

        ItemEntity item = itemRepository.findByItemname(itemName).get();

        item.getSeller().removeItem(item);
        entityManager.flush();

        basketRepository.deleteByItem(item);
        Integer id = item.getItemid();
        //itemRepository.setItemSellerNull(id);
        itemRepository.deleteById(id);
        return itemRepository.buildSuccessItem((ItemEntity) null);
    }

    public ResultVO changeItemName(String oldName, String newName){
        if(itemRepository.isItemNotExist(oldName))
            return buildError(Constants.VO_ITEM_NOT_EXIST);
        if(itemRepository.isItemExist(newName))
            return buildError(Constants.VO_ITEM_ALREADY_EXIST);

        itemRepository.updateItemName(oldName, newName);
        return itemRepository.buildSuccessItem((ItemEntity) null);
    }
}

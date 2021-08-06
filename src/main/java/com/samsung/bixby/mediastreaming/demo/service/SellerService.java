package com.samsung.bixby.mediastreaming.demo.service;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.BasketRepository;
import com.samsung.bixby.mediastreaming.demo.dao.ItemRepository;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.dao.SellerRepository;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.samsung.bixby.mediastreaming.demo.service.helper.Helper.buildError;

@Service
public class SellerService {
    private SellerRepository sellerRepository;
    private BasketRepository basketRepository;
    private ItemRepository itemRepository;
    private EntityManager entityManager;

    @Autowired
    public SellerService(SellerRepository sellerRepository,
                         BasketRepository basketRepository,
                         ItemRepository itemRepository,
                         EntityManager entityManager){
        this.sellerRepository = sellerRepository;
        this.basketRepository = basketRepository;
        this.itemRepository = itemRepository;
        this.entityManager = entityManager;
    }


    public ResultVO getSellerList(){
        List<SellerEntity> sellers = sellerRepository.findAll();
        return sellerRepository.buildSuccessSeller(sellers);
    }

    public ResultVO addSeller(String sellerName){
        if(sellerRepository.SellerExist(sellerName))
            return buildError(Constants.VO_SELLER_ALREADY_EXIST);

        SellerEntity seller = sellerRepository.save(SellerEntity.builder()
                                                                .name(sellerName)
                                                                .password(null)
                                                                .items(new ArrayList<>())
                                                                .build());

        return sellerRepository.buildSuccessSeller(seller);
    }

    public ResultVO deleteSeller(String sellerName){
        if(sellerRepository.SellerNotExist(sellerName))
            return buildError(Constants.VO_SELLER_NOT_EXIST);

        SellerEntity seller = sellerRepository.findByName(sellerName).get();
        List<ItemEntity> items = seller.getItems();


        for(ItemEntity item : items){
            seller.removeItem(item);
        }

        entityManager.flush();

        for(ItemEntity item : items){
            basketRepository.deleteByItem(item);
            Integer id = item.getItemid();
            itemRepository.deleteById(id);
        }

        sellerRepository.deleteById(seller.getId());

        return sellerRepository.buildSuccessSeller((SellerEntity) null);
    }


    public ResultVO changeSeller(String oldName, String newName){
        if(sellerRepository.SellerNotExist(oldName))
            return buildError(Constants.VO_SELLER_NOT_EXIST);
        if(sellerRepository.SellerExist(newName))
            return buildError(Constants.VO_SELLER_ALREADY_EXIST);

        sellerRepository.updateSellerName(oldName, newName);
        return sellerRepository.buildSuccessSeller((SellerEntity) null);
    }

}

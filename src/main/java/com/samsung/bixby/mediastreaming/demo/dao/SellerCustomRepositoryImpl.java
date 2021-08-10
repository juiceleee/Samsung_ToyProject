package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
public class SellerCustomRepositoryImpl implements SellerCustomRepository {

    @Autowired
    @Lazy
    SellerRepository sellerRepository;

    @Autowired
    EntityManager entityManager;


    @Override
    public HashMap<String, String> sellerEntityToMap(List<SellerEntity> sellers){
        HashMap<String, String> map = new HashMap<>();
        for(SellerEntity seller: sellers){
            map.put(seller.getName(), "");
        }
        return map;
    }

    @Override
    public HashMap<String, String> sellerEntityToMap(SellerEntity seller){
        HashMap<String, String> map = new HashMap<>();
        map.put(seller.getName(), "");
        return map;
    }

    @Override
    public ResultVO buildSuccessSeller(List<SellerEntity> sellers){
        return ResultVO.builder()
                .map(sellerEntityToMap(sellers))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    @Override
    public ResultVO buildSuccessSeller(SellerEntity seller){

        if(seller == null)
            return ResultVO.builder()
                    .map(null)
                    .status(Constants.VO_SUCCESS)
                    .build();

        return ResultVO.builder()
                .map(sellerEntityToMap(seller))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    @Override
    public void nullItem(String sellerName){
        SellerEntity seller = sellerRepository.findByName(sellerName).get();
        seller.nullItem();
        entityManager.flush();
    }

    @Override
    public boolean SellerExist(String sellerName){
        return sellerRepository.findByName(sellerName).isPresent();
    }

    @Override
    public boolean SellerNotExist(String sellerName){
        return !SellerExist(sellerName);
    }

}

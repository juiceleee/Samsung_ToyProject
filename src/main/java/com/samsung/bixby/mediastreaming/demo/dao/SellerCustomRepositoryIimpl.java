package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class SellerCustomRepositoryIimpl implements SellerCustomRepository {
    @Autowired
    SellerRepository sellerRepository;


    public HashMap<String, String> sellerEntityToMap(List<SellerEntity> sellers){
        HashMap<String, String> map = new HashMap<>();
        for(SellerEntity seller: sellers){
            map.put(seller.getName(), "");
        }
        return map;
    }

    public HashMap<String, String> sellerEntityToMap(SellerEntity seller){
        HashMap<String, String> map = new HashMap<>();
        map.put(seller.getName(), "");
        return map;
    }

    public ResultVO buildSuccessSeller(List<SellerEntity> sellers){
        return ResultVO.builder()
                .map(sellerEntityToMap(sellers))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public ResultVO buildSuccessSeller(SellerEntity seller){
        return ResultVO.builder()
                .map(sellerEntityToMap(seller))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public boolean isSellerExist(String sellerName){
        return sellerRepository.findByName(sellerName).isPresent();
    }

    public boolean isSellerNotExist(String sellerName){
        return !isSellerExist(sellerName);
    }

}

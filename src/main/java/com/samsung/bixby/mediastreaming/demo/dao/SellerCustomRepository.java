package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;

import java.util.HashMap;
import java.util.List;

public interface SellerCustomRepository {
    HashMap<String, String> sellerEntityToMap(List<SellerEntity> sellers);
    HashMap<String, String> sellerEntityToMap(SellerEntity seller);
    ResultVO buildSuccessSeller(List<SellerEntity> sellers);
    ResultVO buildSuccessSeller(SellerEntity seller);
    boolean SellerExist(String sellerName);
    boolean SellerNotExist(String sellerName);
    public void nullItem(String sellerName);

}

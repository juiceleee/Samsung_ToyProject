package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;

import java.util.HashMap;
import java.util.List;

public interface BasketCustomRepository {
    HashMap<String, String> basketEntityToMap(List<BasketEntity> userBasket);
    HashMap<String, String> basketEntityToMap(BasketEntity basket);
    ResultVO buildSuccessBasket(List<BasketEntity> basket);
    ResultVO buildSuccessBasket(BasketEntity basket);
}

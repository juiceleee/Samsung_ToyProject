package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.BasketCustomRepository;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
public class BasketCustomRepositoryImpl implements BasketCustomRepository {

    @Override
    public HashMap<String, String> basketEntityToMap(List<BasketEntity> userBasket){
        HashMap<String, String> map = new HashMap<>();
        for(BasketEntity basket: userBasket){

            map.put(basket.getItem().getItemname()
                    ,basket.getItemcnt().toString());
        }
        return map;
    }

    @Override
    public HashMap<String, String> basketEntityToMap(BasketEntity basket){
        HashMap<String, String> map = new HashMap<>();
        if(basket == null) return null;

        map.put(basket.getItem().getItemname()
                ,basket.getItemcnt().toString());
        return map;
    }

    @Override
    public ResultVO buildSuccessBasket(List<BasketEntity> basket){
        return ResultVO.builder()
                .map(basketEntityToMap(basket))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public ResultVO buildSuccessBasket(BasketEntity basket){
        return ResultVO.builder()
                .map(basketEntityToMap(basket))
                .status(Constants.VO_SUCCESS)
                .build();
    }

}

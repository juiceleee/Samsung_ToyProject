package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
@Transactional
public class UserCustomRepositoryImpl implements UserCustomRepository{

    @Autowired
    @Lazy
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public void nullBasket(String userName){
        UserEntity user = userRepository.findByUsername(userName).get();
        user.nullBasket();
        entityManager.flush();
    }

    @Override
    public boolean UserExist(String userName){
        return userRepository.findByUsername(userName).isPresent();
    }

    @Override
    public boolean UserNotExist(String userName){
        return !UserExist(userName);
    }

    @Override
    public ResultVO buildSuccessUser(List<UserEntity> users){
        return ResultVO.builder()
                .map(userEntityToMap(users))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    @Override
    public ResultVO buildSuccessUser(UserEntity user){
        return ResultVO.builder()
                .map(userEntityToMap(user))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    @Override
    public HashMap<String, String> userEntityToMap(UserEntity user){
        HashMap<String, String> map = new HashMap<>();
        if(user==null) return map;

        map.put(user.getUsername(), "");

        return map;
    }

    @Override
    public HashMap<String, String> userEntityToMap(List<UserEntity> users){
        HashMap<String, String> map = new HashMap<>();
        for(UserEntity user: users){
            map.put(user.getUsername(), "");
        }
        return map;
    }
}

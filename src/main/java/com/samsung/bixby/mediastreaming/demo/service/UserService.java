package com.samsung.bixby.mediastreaming.demo.service;


import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.BasketRepository;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.BasketEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.ItemEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import com.samsung.bixby.mediastreaming.demo.dao.UserRepository;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.samsung.bixby.mediastreaming.demo.service.helper.Helper.buildError;

@Service
public class UserService {
    private UserRepository userRepository;
    private BasketRepository basketRepository;

    @Autowired
    public UserService(UserRepository userRepository, BasketRepository basketRepository){
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
    }

    public ResultVO getUserList() {
        List<UserEntity> users = userRepository.findAll();
        return userRepository.buildSuccessUser(users);
    }

    public ResultVO addUser(String userName){
        if(userRepository.isUserExist(userName))
            return buildError(Constants.VO_USER_ALREADY_EXIST);

        UserEntity user = userRepository.save(UserEntity.builder()
                .username(userName)
                .password(null)
                .baskets(null)
                .baskets(null).build());
        return userRepository.buildSuccessUser(user);
    }

    public ResultVO deleteUser(String userName){
        UserEntity user = userRepository.findByUsername(userName).get();
        List<BasketEntity> baskets = user.getBaskets();

        userRepository.setBasketsNull(user.getUserid());

        basketRepository.deleteByUser(user);

        userRepository.deleteById(user.getUserid());

        return userRepository.buildSuccessUser((UserEntity) null);
    }

    public ResultVO changeUser(String oldName, String newName){
        if(userRepository.isUserNotExist(oldName))
            return buildError(Constants.VO_USER_NOT_EXIST);
        if(userRepository.isUserExist(newName))
            return buildError(Constants.VO_USER_ALREADY_EXIST);

        userRepository.updateUserName(oldName, newName);
        return userRepository.buildSuccessUser((UserEntity) null);
    }
}

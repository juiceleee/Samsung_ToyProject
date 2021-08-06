package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class UserCustomRepositoryImpl implements UserCustomRepository{
    @Autowired
    UserRepository userRepository;

    public boolean isUserExist(String userName){
        return userRepository.findByUsername(userName).isPresent();
    }

    public boolean isUserNotExist(String userName){
        return !isUserExist(userName);
    }

    public ResultVO buildSuccessUser(List<UserEntity> users){
        return ResultVO.builder()
                .map(userEntityToMap(users))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public ResultVO buildSuccessUser(UserEntity user){
        return ResultVO.builder()
                .map(userEntityToMap(user))
                .status(Constants.VO_SUCCESS)
                .build();
    }

    public HashMap<String, String> userEntityToMap(UserEntity user){
        HashMap<String, String> map = new HashMap<>();
        if(user==null) return map;

        map.put(user.getUsername(), "");

        return map;
    }

    public HashMap<String, String> userEntityToMap(List<UserEntity> users){
        HashMap<String, String> map = new HashMap<>();
        for(UserEntity user: users){
            map.put(user.getUsername(), "");
        }
        return map;
    }
}

package com.samsung.bixby.mediastreaming.demo.dao;

import com.samsung.bixby.mediastreaming.demo.dao.entitiy.UserEntity;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;

import java.util.HashMap;
import java.util.List;

public interface UserCustomRepository {

    boolean UserExist(String userName);
    boolean UserNotExist(String userName);
    public ResultVO buildSuccessUser(List<UserEntity> users);
    public ResultVO buildSuccessUser(UserEntity user);
    public HashMap<String, String> userEntityToMap(UserEntity user);
    public HashMap<String, String> userEntityToMap(List<UserEntity> users);
    public void nullBasket(String userName);
}

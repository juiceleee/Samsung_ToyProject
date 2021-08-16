package com.samsung.bixby.mediastreaming.demo.controller.helper;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.vo.ResponseVO;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class Helper {

    public static ResponseVO makeBadReqBody(String str) {
        ResponseVO responseVO = new ResponseVO(new HashMap<>());
        responseVO.getResult().put("errorMessage", str);
        return responseVO;
    }

    public static ResponseEntity<ResponseVO> makeResponse(ResultVO resultVO, HttpStatus ifSuccess){
        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                if(ifSuccess.equals(HttpStatus.OK))
                    return new ResponseEntity<>(new ResponseVO(resultVO.getMap()), HttpStatus.OK);
                else if(ifSuccess.equals(HttpStatus.CREATED))
                    return new ResponseEntity<>(new ResponseVO(resultVO.getMap()), HttpStatus.CREATED);
                else if(ifSuccess.equals(HttpStatus.NO_CONTENT))
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                else
                    return new ResponseEntity<>(makeBadReqBody("Success but not good"), HttpStatus.INTERNAL_SERVER_ERROR);
            case Constants.VO_USER_NOT_EXIST:
                return new ResponseEntity<>(makeBadReqBody("User not exists!"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_NOT_EXIST:
                return new ResponseEntity<>(makeBadReqBody("Item not exists!"), HttpStatus.BAD_REQUEST);
            case Constants.VO_USER_ALREADY_EXIST:
                return new ResponseEntity<>(makeBadReqBody("User Already exists"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_ALREADY_EXIST:
                return new ResponseEntity<>(makeBadReqBody("Item already exists"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_CNT_TOO_MUCH:
                return new ResponseEntity<>(makeBadReqBody("Item cnt too much"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_OUT_OF_STOCK:
                return new ResponseEntity<>(makeBadReqBody("Item out of stock"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_NOT_IN_BASKET:
                return new ResponseEntity<>(makeBadReqBody("Item not in basket"), HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_OWNER_NOT_SAME:
                return new ResponseEntity<>(makeBadReqBody("Item owner not same"), HttpStatus.BAD_REQUEST);
            case Constants.VO_SELLER_ALREADY_EXIST:
                return new ResponseEntity<>(makeBadReqBody("Seller already exist"), HttpStatus.BAD_REQUEST);
            case Constants.VO_SELLER_NOT_EXIST:
                return new ResponseEntity<>(makeBadReqBody("Seller not exist"), HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.samsung.bixby.mediastreaming.demo.controller.helper;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class Helper {

    public static HashMap<String, String> makeBadReqBody(String str) {
        HashMap<String, String> result = new HashMap<>();
        result.put("errorMessage", str);
        return result;
    }

    public static ResponseEntity<HashMap<String, String>> makeResponse(ResultVO resultVO, HttpStatus ifSuccess){
        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                if(ifSuccess.equals(HttpStatus.OK))
                    return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
                else if(ifSuccess.equals(HttpStatus.CREATED))
                    return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
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
            default:
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

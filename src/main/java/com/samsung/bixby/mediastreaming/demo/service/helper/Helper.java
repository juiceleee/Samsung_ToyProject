package com.samsung.bixby.mediastreaming.demo.service.helper;

import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;

public class Helper {

    public static ResultVO buildError(Integer status) {
        return ResultVO.builder()
                .map(null)
                .status(status)
                .build();
    }
}

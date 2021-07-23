package com.samsung.bixby.mediastreaming.demo.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchResponseVOTest {
    @Test
    public void memEqual(){
        String resultCode = "ok";
        String resultMessage = "GOOD";
        String resultTraceId = "1";
        String[] resultData = {"123", "456"};
        SearchResponseVO vo = new SearchResponseVO(resultCode, resultMessage, resultTraceId, resultData);
        assertEquals(resultCode, vo.getResultCode());
        assertEquals(resultMessage, vo.getResultMessage());
        assertEquals(resultTraceId, vo.getResultTraceId());
        assertEquals(resultData, vo.getResultData());
    }
}
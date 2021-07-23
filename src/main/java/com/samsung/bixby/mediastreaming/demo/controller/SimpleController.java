package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.vo.SearchResponseVO;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.awt.*;

@RestController
@Api(tags = "/test API")
@RequestMapping("api/v1/media/search")
@ApiResponses(value = {@ApiResponse(code=404, message = "Not Found"),
                        @ApiResponse(code = 500, message = "Internal Server Error")})
public class SimpleController {
    //parameter setting for swagger2
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "x-user-id", dataType = "string", value = "user id", required = true),
            @ApiImplicitParam(paramType = "header", name = "x-svc-id", dataType = "string", value = "service id", required = true),
            @ApiImplicitParam(paramType = "header", name = "x-device-id", dataType = "string", value = "device id", required = true),
            @ApiImplicitParam(paramType = "header", name = "x-conversation-id", dataType = "string", value = "conversatoin id", required = true),
            @ApiImplicitParam(paramType = "header", name = "x-request-id", dataType = "string", value = "request id", required = true),
            @ApiImplicitParam(paramType = "header", name = "x-user-locale", dataType = "string", value = "user language", required = true),
    })
    @GetMapping("/keyword")
    @ApiOperation(value = "Search media based on keyword", response = ResponseEntity.class, notes = "require parameters")
    @ResponseBody
    public SearchResponseVO getMediabyKeyword(
            @RequestHeader(value = "x-user-id") String userId,
            @RequestHeader(value = "x-svc-id") String svcId,
            @RequestHeader(value = "x-device-id") String devId,
            @RequestHeader(value = "x-conversation-id") String convId,
            @RequestHeader(value = "x-request-id") String reqId,
            @RequestHeader(value = "x-user-locale") String locale,
            @ApiParam(name = "keyword", value = "media keyword", required = true)
            @RequestParam(value = "keyword") String keyword,
            @ApiParam(name = "count", value = "count", required = true)
            @RequestParam(value = "count") Integer count,
            @ApiParam(name = "pageoffset", value = "pageoffset", required = true)
            @RequestParam(value = "pageoffset") Integer pageoffset,
            @ApiParam(name = "newerthan", value="newerthan", required = true)
            @RequestParam(value = "newerthan") Integer newerthan
    ){
        String[] result_data = {"Nothing", "at", "all"};
        SearchResponseVO responseVO = new SearchResponseVO("200", "GOOD", "123", result_data);
        return responseVO;
    }
}

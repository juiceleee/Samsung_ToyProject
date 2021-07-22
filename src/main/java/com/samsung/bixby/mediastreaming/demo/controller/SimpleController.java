package com.samsung.bixby.mediastreaming.demo.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@Api(tags = "/test API")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses(value = {@ApiResponse(code=404, message = "Not Found"),
                        @ApiResponse(code = 500, message = "Internal Server Error")})
public class SimpleController {
    @GetMapping("/test/info")
    @ApiOperation(value = "Get info", response = String.class, notes = "test get info")
    public String getInfo(){
        return "Getting info success";
    }
}

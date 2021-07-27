package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.service.MediaSearchService;
import com.samsung.bixby.mediastreaming.demo.vo.AddItemResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResponseVO;
import com.samsung.bixby.mediastreaming.demo.vo.SearchResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.AddItemResponseVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.awt.*;
import java.util.ArrayList;

@RestController
@Api(tags = "/shopping list API")
@RequestMapping("shopping")
@ApiResponses(value = {@ApiResponse(code=404, message = "Not Found"),
                        @ApiResponse(code = 500, message = "Internal Server Error")})
public class SimpleController {
    private MediaSearchService userService;

    @Autowired
    public SimpleController(MediaSearchService userService) {
        this.userService = userService;
    }

    //GET method
    @GetMapping("/list/{userId}")
    @ApiOperation(value = "Browse shopping list by userid", response = SearchResultVO.class, notes = "null if no user information")
    @ResponseBody
    public SearchResponseVO getShoppingList(@PathVariable String userId){
        SearchResultVO resultVO = userService.findShoppingListById(userId);
        if(resultVO != null)
            return SearchResponseVO.builder()
                    .resultCode("200")
                    .resultData(resultVO.getShoppingList())
                    .build();
        else
            return SearchResponseVO.builder()
                    .resultCode("500")
                    .resultData(null)
                    .build();
    }

    //POST method
    @PostMapping("/list/{userId}/{itemId}/{itemCnt}")
    @ApiOperation(value = "Add new item to shopping list", response = SearchResultVO.class)
    @ResponseBody
    public SearchResponseVO addShoppingList(@PathVariable String userId, @PathVariable Integer itemId, @PathVariable Integer itemCnt){
        SearchResultVO resultV0 = userService.addShoppingListById(userId, itemId, itemCnt);
        if(resultV0!= null)
            return SearchResponseVO.builder()
                                    .resultCode("201")
                                    .resultData(resultV0.getShoppingList())
                                    .build();
        else
            return SearchResponseVO.builder()
                                    .resultCode("500")
                                    .resultData(null)
                                    .build();
    }

    @GetMapping("/item")
    @ApiOperation(value = "Get item list")
    @ResponseBody
    public AddItemResponseVO getItem(){
        AddItemResultVO resultVO = userService.getItemList();
        return AddItemResponseVO.builder()
                                .resultCode("200")
                                .map(resultVO.getMap())
                                .build();
    }

    @PostMapping("/item/{itemName}/{itemNum}")
    @ApiOperation(value = "Add new product")
    @ResponseBody
    public AddItemResponseVO addItem(@PathVariable String itemName, @PathVariable Integer itemNum){
        AddItemResultVO resultVO = userService.addItemByName(itemName, itemNum);
        if(resultVO != null)
            return AddItemResponseVO.builder()
                                    .resultCode("201")
                                    .map(resultVO.getMap())
                                    .build();
        else
            return AddItemResponseVO.builder()
                                    .resultCode("500")
                                    .map(null)
                                    .build();
    }




}

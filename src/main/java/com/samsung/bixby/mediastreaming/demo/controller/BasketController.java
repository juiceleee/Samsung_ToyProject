package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.BasketService;
import com.samsung.bixby.mediastreaming.demo.vo.BasketRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.samsung.bixby.mediastreaming.demo.controller.helper.Helper.makeBadReqBody;
import static com.samsung.bixby.mediastreaming.demo.controller.helper.Helper.makeResponse;

@RestController
@Api(tags = "Basket controller")
@RequestMapping("/shopping/basket")
public class BasketController {

    private BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService){
        this.basketService = basketService;
    }

    @GetMapping
    @ApiOperation(value = "Browse shopping list by username", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getShoppingList(@RequestBody BasketRequestVO basketRequestVO){
        ResultVO resultVO = basketService.getShoppingListById(basketRequestVO.getUserName());

        return makeResponse(resultVO, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Add new item to shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addShoppingList(@RequestBody BasketRequestVO basketRequestVO){
        ResultVO resultVO = basketService.addShoppingListById(basketRequestVO.getUserName(),
                basketRequestVO.getItemName(),
                basketRequestVO.getItemCnt());

        return makeResponse(resultVO, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value="Update item from shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> updateItemEntry(@RequestBody BasketRequestVO basketRequestVO){
        String userName = basketRequestVO.getUserName();
        String itemName = basketRequestVO.getItemName();
        Integer itemCnt = basketRequestVO.getItemCnt();

        ResultVO resultVO = basketService.updateItemFromShoppingList(userName, itemName, itemCnt);

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation(value="Delete item from shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteItemEntry(@RequestBody BasketRequestVO basketRequestVO){

        String userName = basketRequestVO.getUserName();
        String itemName = basketRequestVO.getItemName();

        ResultVO resultVO;

        if(itemName == null)
            resultVO = basketService.deleteItemFromShoppingList(userName);
        else
            resultVO = basketService.deleteItemFromShoppingList(userName, itemName);


        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/status")
    @ApiOperation(value="Buying item in shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> buyItemEntry(@RequestBody BasketRequestVO basketRequestVO){
        String userName = basketRequestVO.getUserName();
        String itemName = basketRequestVO.getItemName();

        ResultVO resultVO = basketService.confirmBuying(userName, itemName);

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/status")
    @ApiOperation(value="show bought items", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getBoughtItem(@RequestBody BasketRequestVO basketRequestVO){
        String userName = basketRequestVO.getUserName();
        String itemName = basketRequestVO.getItemName();

        ResultVO resultVO = basketService.getBoughtItem(userName);

        return makeResponse(resultVO, HttpStatus.OK);
    }

}

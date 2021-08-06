package com.samsung.bixby.mediastreaming.demo.controller;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.ItemService;
import com.samsung.bixby.mediastreaming.demo.vo.ItemRequestVO;
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
@Api(tags = "itemService")
@RequestMapping("shopping/item")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping
    @ApiOperation(value = "Get all item list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getAllItem(){
        ResultVO resultVO = itemService.getItemList();

        return makeResponse(resultVO, HttpStatus.OK);
    }


    @GetMapping("/seller")
    @ApiOperation(value = "Get item list by seller", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getItem(@RequestBody ItemRequestVO itemRequestVO) {
        ResultVO resultVO = itemService.getItemListBySellerName(itemRequestVO.getSellerName());

        return makeResponse(resultVO, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Add new item", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addItem(@RequestBody ItemRequestVO itemRequestVO){
        ResultVO resultVO = itemService.addItemByName(itemRequestVO.getSellerName(), itemRequestVO.getItemName(), itemRequestVO.getStock());

            return makeResponse(resultVO, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "Change item number", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> updateItem(@RequestBody ItemRequestVO itemRequestVO){
        String userName = itemRequestVO.getSellerName();
        String itemName = itemRequestVO.getItemName();
        Integer itemStock = itemRequestVO.getStock();

        ResultVO resultVO = itemService.updateItem(userName, itemName, itemStock);

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete item", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteItem(@RequestBody ItemRequestVO itemRequestVO){

        String itemName = itemRequestVO.getItemName();

        ResultVO resultVO = itemService.deleteItem(itemName);


        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/name")
    @ApiOperation(value = "Change the name of item", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> changeItemName(@RequestBody ItemRequestVO itemRequestVO){
        ResultVO resultVO = itemService.changeItemName(itemRequestVO.getOldName(), itemRequestVO.getNewName());

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);

    }
}

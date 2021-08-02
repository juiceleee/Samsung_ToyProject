package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.ShoppingService;
import com.samsung.bixby.mediastreaming.demo.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Api(tags = "/shopping list API")
@RequestMapping("shopping")
@ApiResponses(value = {@ApiResponse(code=404, message = "Not Found"),
                        @ApiResponse(code = 500, message = "Internal Server Error")})
public class SimpleController {
    private ShoppingService userService;

    public HashMap<String, String> makeBadReqBody(String str) {
        HashMap<String, String> result = new HashMap<>();
        result.put("errorMessage", str);
        return result;
    }

    @Autowired
    public SimpleController(ShoppingService userService) {
        this.userService = userService;
    }


    /** /user methods **/
    @GetMapping("/user")
    @ApiOperation(value = "Get current user list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getUserList(){
        ResultVO resultVO = userService.getUserList();
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /user}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/user")
    @ApiOperation(value = "Add new user to DB", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.addUser(userRequestVO.getUserName());
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            case Constants.VO_USER_ALREADY_EXIST:
                httpHeaders.add("errorMessage", "User already exists!");
                return new ResponseEntity<>(makeBadReqBody("User already exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /user/{username}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "Delete user specified by username", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.deleteUser(userRequestVO.getUserName());
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exist!");
                return new ResponseEntity<>(makeBadReqBody("User not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(DELETE /user/{username}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user")
    @ApiOperation(value = "Change user name to new one", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> changeUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.changeUser(userRequestVO.getOldUserName(), userRequestVO.getNewUserName());
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exist!");
                return new ResponseEntity<>(makeBadReqBody("User not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_USER_ALREADY_EXIST:
                httpHeaders.add("errorMessage", "New name already used! Pick another one");
                return new ResponseEntity<>(makeBadReqBody("New name already used! Pick another one"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(DELETE /user/{username}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /** /basket methods **/
    @GetMapping("/basket")
    @ApiOperation(value = "Browse shopping list by username", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getShoppingList(@RequestBody BasketRequestVO basketRequestVO){
        HttpHeaders httpHeaders = new HttpHeaders();
        ResultVO resultVO = userService.getShoppingListById(basketRequestVO.getUserName());

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exists!");
                return new ResponseEntity<>(makeBadReqBody("User not exists"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /list/{username}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/basket")
    @ApiOperation(value = "Add new item to shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addShoppingList(@RequestBody BasketRequestVO basketRequestVO){
        HttpHeaders httpHeaders = new HttpHeaders();
        ResultVO resultVO = userService.addShoppingListById(basketRequestVO.getUserName(),
                                                            basketRequestVO.getItemName(),
                                                            basketRequestVO.getItemCnt());

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exists!");
                return new ResponseEntity<>(makeBadReqBody("User not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_NOT_EXIST:
                httpHeaders.add("errorMessage", "Item not exists!");
                return new ResponseEntity<>(makeBadReqBody("Item not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /list/{username}/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/basket")
    @ApiOperation(value="Delete item from shopping list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteItemEntry(@RequestBody BasketRequestVO basketRequestVO){

        String userName = basketRequestVO.getUserName();
        String itemName = basketRequestVO.getItemName();
        Integer itemCnt = basketRequestVO.getItemCnt();

        HttpHeaders httpHeaders = new HttpHeaders();
        ResultVO resultVO;

        if(itemName == null)
            resultVO = userService.deleteItemFromShoppingList(userName);
        else if(itemCnt == null)
            resultVO = userService.deleteItemFromShoppingList(userName, itemName);
        else
            resultVO = userService.deleteItemFromShoppingList(userName, itemName, itemCnt);

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exists!");
                return new ResponseEntity<>(makeBadReqBody("User not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_NOT_EXIST:
                httpHeaders.add("errorMessage", "Item not exists!");
                return new ResponseEntity<>(makeBadReqBody("Item not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_CNT_TOO_MUCH:
                httpHeaders.add("errorMessage", "Deleting too many item!");
                return new ResponseEntity<>(makeBadReqBody("Deleting too many item!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /list/{username}/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /** /item methods **/

    @GetMapping("/item")
    @ApiOperation(value = "Get item list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getItem(){
        ResultVO resultVO = userService.getItemList();
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /item");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/item")
    @ApiOperation(value = "Add new product", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addItem(@RequestBody ItemRequestVO itemRequestVO){
        ResultVO resultVO = userService.addItemByName(itemRequestVO.getItemName(), itemRequestVO.getItemCnt());
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            case Constants.VO_ITEM_ALREADY_EXIST:
                httpHeaders.add("errorMessage", "Item already exists!");
                return new ResponseEntity<>(makeBadReqBody("Item already exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /item/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/item")
    @ApiOperation(value = "Delete item", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteItem(@RequestBody ItemRequestVO itemRequestVO){

        String itemName = itemRequestVO.getItemName();
        Integer itemCnt = itemRequestVO.getItemCnt();

        ResultVO resultVO;
        HttpHeaders httpHeaders = new HttpHeaders();

        if(itemCnt == null)
            resultVO = userService.deleteItem(itemName);
        else
            resultVO = userService.deleteItem(itemName, itemCnt);

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            case Constants.VO_ITEM_NOT_EXIST:
                httpHeaders.add("errorMessage", "Item not exists!");
                return new ResponseEntity<>(makeBadReqBody("Item not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_CNT_TOO_MUCH:
                httpHeaders.add("errorMessage", "Removing too much items!");
                return new ResponseEntity<>(makeBadReqBody("Removing too much items!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /item/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/item")
    @ApiOperation(value = "Change the name of item", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> changeItemName(@RequestBody ItemRequestVO itemRequestVO){
        ResultVO resultVO = userService.changeItemName(itemRequestVO.getOldName(), itemRequestVO.getNewName());
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            case Constants.VO_ITEM_NOT_EXIST:
                httpHeaders.add("errorMessage", "Item not exists!");
                return new ResponseEntity<>(makeBadReqBody("Item not exists!"), httpHeaders, HttpStatus.BAD_REQUEST);
            case Constants.VO_ITEM_ALREADY_EXIST:
                httpHeaders.add("errorMessage", "newName already used!");
                return new ResponseEntity<>(makeBadReqBody("newName already used!"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /item/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}

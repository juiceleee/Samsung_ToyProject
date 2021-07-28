package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.MediaSearchService;
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
    private MediaSearchService userService;

    public HashMap<String, String> makeBadReqBody(String str) {
        HashMap<String, String> result = new HashMap<>();
        result.put("errorMessage", str);
        return result;
    }

    @Autowired
    public SimpleController(MediaSearchService userService) {
        this.userService = userService;
    }


    /** /user methods **/
    @GetMapping("/user")
    @ApiOperation(value = "Get current user list")
    public ResponseEntity<HashMap<String, String>> getUserList(){
        UserResultVO resultVO = userService.getUserList();
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /user}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/user/{userName}")
    @ApiOperation(value = "Add new user to DB")
    public ResponseEntity<HashMap<String, String>> addUser(@PathVariable String userName){
        UserResultVO resultVO = userService.addUser(userName);
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

    @DeleteMapping("/user/{userName}")
    @ApiOperation(value = "Delete user specified by username")
    public ResponseEntity<HashMap<String, String>> deleteUser(@PathVariable String userName){
        UserResultVO resultVO = userService.deleteUser(userName);
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

    @PutMapping("/user/{oldUserName}/{newUserName}")
    @ApiOperation(value = "Change user name to new one")
    public ResponseEntity<HashMap<String, String>> changeUser(@PathVariable String oldUserName, @PathVariable String newUserName){
        UserResultVO resultVO = userService.changeUser(oldUserName, newUserName);
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
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


    /** /list methods **/
    //GET method
    @GetMapping("/list/{userName}")
    @ApiOperation(value = "Browse shopping list by username", response = SearchResultVO.class, notes = "null if no user information")
    public ResponseEntity<HashMap<String, String>> getShoppingList(@PathVariable String userName){
        HttpHeaders httpHeaders = new HttpHeaders();
        SearchResultVO resultVO = userService.findShoppingListById(userName);

        switch(resultVO.getStatus()) {
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getShoppingList(), HttpStatus.OK);
            case Constants.VO_USER_NOT_EXIST:
                httpHeaders.add("errorMessage", "User not exists!");
                return new ResponseEntity<>(makeBadReqBody("User not exists"), httpHeaders, HttpStatus.BAD_REQUEST);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /list/{username}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //POST method
    @PostMapping("/list/{userName}/{itemName}/{itemCnt}")
    @ApiOperation(value = "Add new item to shopping list", response = SearchResultVO.class)
    public ResponseEntity<HashMap<String, String>> addShoppingList(@PathVariable String userName, @PathVariable String itemName, @PathVariable Integer itemCnt){
        HttpHeaders httpHeaders = new HttpHeaders();
        SearchResultVO resultVO = userService.addShoppingListById(userName, itemName, itemCnt);

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getShoppingList(), HttpStatus.CREATED);
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

    /** /item methods **/

    @GetMapping("/item")
    @ApiOperation(value = "Get item list")
    public ResponseEntity<HashMap<String, String>> getItem(){
        ItemResultVO resultVO = userService.getItemList();
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.OK);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(GET /item");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/item/{itemName}/{itemCnt}")
    @ApiOperation(value = "Add new product")
    public ResponseEntity<HashMap<String, String>> addItem(@PathVariable String itemName, @PathVariable Integer itemCnt){
        ItemResultVO resultVO = userService.addItemByName(itemName, itemCnt);
        HttpHeaders httpHeaders = new HttpHeaders();

        switch(resultVO.getStatus()){
            case Constants.VO_SUCCESS:
                return new ResponseEntity<>(resultVO.getMap(), HttpStatus.CREATED);
            default:
                httpHeaders.add("errorMessage", "Should not reach here(POST /item/{itemName}/{itemCnt}");
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}

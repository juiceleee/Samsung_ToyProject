package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.service.MediaSearchService;
import com.samsung.bixby.mediastreaming.demo.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /** /user methods **/
    @GetMapping("/user")
    @ApiOperation(value = "Get current user list")
    public UserResponseVO getUserList(){
        return UserResponseVO.builder()
                                .resultCode("200")
                                .map(userService.getUserList().getMap()).build();

    }

    @PostMapping("/user/{userName}")
    @ApiOperation(value = "Add new user to DB")
    @ResponseBody
    public UserResponseVO addUser(@PathVariable String userName){
        UserResultVO resultVO = userService.addUser(userName);
        if(resultVO != null)
            return UserResponseVO.builder()
                                    .resultCode("201")
                                    .map(resultVO.getMap())
                                    .build();
        else
            return UserResponseVO.builder()
                                    .resultCode("500")
                                    .map(null)
                                    .build();
    }

//    @DeleteMapping("/user/{userName}")
//    @ApiOperation(value = "Delete user specified by username")
//    @ResponseBody
//    public UserResponseVO deleteUser(@PathVariable String userName){
//        UserResultVO resultVO = userService.deleteUser(userName);
//
//    }


    /** /list methods **/
    //GET method
    @GetMapping("/list/{userName}")
    @ApiOperation(value = "Browse shopping list by username", response = SearchResultVO.class, notes = "null if no user information")
    @ResponseBody
    public SearchResponseVO getShoppingList(@PathVariable String userName){
        SearchResultVO resultVO = userService.findShoppingListById(userName);
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
    @PostMapping("/list/{userName}/{itemName}/{itemCnt}")
    @ApiOperation(value = "Add new item to shopping list", response = SearchResultVO.class)
    @ResponseBody
    public SearchResponseVO addShoppingList(@PathVariable String userName, @PathVariable String itemName, @PathVariable Integer itemCnt){
        SearchResultVO resultV0 = userService.addShoppingListById(userName, itemName, itemCnt);
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

    /** /item methods **/

    @GetMapping("/item")
    @ApiOperation(value = "Get item list")
    @ResponseBody
    public ItemResponseVO getItem(){
        ItemResultVO resultVO = userService.getItemList();
        return ItemResponseVO.builder()
                                .resultCode("200")
                                .map(resultVO.getMap())
                                .build();
    }

    @PostMapping("/item/{itemName}/{itemCnt}")
    @ApiOperation(value = "Add new product")
    @ResponseBody
    public ItemResponseVO addItem(@PathVariable String itemName, @PathVariable Integer itemCnt){
        ItemResultVO resultVO = userService.addItemByName(itemName, itemCnt);
        if(resultVO != null)
            return ItemResponseVO.builder()
                                    .resultCode("201")
                                    .map(resultVO.getMap())
                                    .build();
        else
            return ItemResponseVO.builder()
                                    .resultCode("500")
                                    .map(null)
                                    .build();
    }




}

package com.samsung.bixby.mediastreaming.demo.controller;


import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.UserService;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.samsung.bixby.mediastreaming.demo.controller.helper.Helper.makeBadReqBody;
import static com.samsung.bixby.mediastreaming.demo.controller.helper.Helper.makeResponse;

@RestController
@Api(tags = "userService")
@RequestMapping("shopping/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /** /user methods **/
    @GetMapping
    @ApiOperation(value = "Get current user list", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> getUserList(){
        ResultVO resultVO = userService.getUserList();

        return makeResponse(resultVO, HttpStatus.OK);

    }

    @PostMapping
    @ApiOperation(value = "Add new user to DB", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> addUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.addUser(userRequestVO.getUserName());

        return makeResponse(resultVO, HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete user specified by username", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> deleteUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.deleteUser(userRequestVO.getUserName());

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @ApiOperation(value = "Change user name to new one", response = ResponseEntity.class)
    public ResponseEntity<HashMap<String, String>> changeUser(@RequestBody UserRequestVO userRequestVO){
        ResultVO resultVO = userService.changeUser(userRequestVO.getOldUserName(), userRequestVO.getNewUserName());

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }



}

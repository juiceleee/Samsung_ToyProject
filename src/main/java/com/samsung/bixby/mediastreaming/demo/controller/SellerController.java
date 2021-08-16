package com.samsung.bixby.mediastreaming.demo.controller;

import com.samsung.bixby.mediastreaming.demo.common.Constants;
import com.samsung.bixby.mediastreaming.demo.service.SellerService;
import com.samsung.bixby.mediastreaming.demo.vo.ResponseVO;
import com.samsung.bixby.mediastreaming.demo.vo.ResultVO;
import com.samsung.bixby.mediastreaming.demo.vo.SellerRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
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
@Api(tags = "Seller controller")
@RequestMapping("/shopping/seller")
public class SellerController {
    private SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }

    @GetMapping
    @ApiOperation(value = "Get seller user list", response = ResponseEntity.class)
    public ResponseEntity<ResponseVO> getSellerList(){
        ResultVO resultVO = sellerService.getSellerList();

        return makeResponse(resultVO, HttpStatus.OK);

    }

    @PostMapping
    @ApiOperation(value = "Add new seller to DB", response = ResponseEntity.class)
    public ResponseEntity<ResponseVO> addSeller(@RequestBody SellerRequestVO sellerRequestVO){
        ResultVO resultVO = sellerService.addSeller(sellerRequestVO.getSellerName());

        return makeResponse(resultVO, HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete user specified by username", response = ResponseEntity.class)
    public ResponseEntity<ResponseVO> deleteSeller(@RequestBody SellerRequestVO sellerRequestVO){
        ResultVO resultVO = sellerService.deleteSeller(sellerRequestVO.getSellerName());

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @ApiOperation(value = "Change user name to new one", response = ResponseEntity.class)
    public ResponseEntity<ResponseVO> changeUser(@RequestBody SellerRequestVO sellerRequestVO){
        ResultVO resultVO = sellerService.changeSeller(sellerRequestVO.getOldSellerName(),
                                                        sellerRequestVO.getNewSellerName());

        return makeResponse(resultVO, HttpStatus.NO_CONTENT);
    }


}

package com.samsung.bixby.mediastreaming.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class UserRequestVO {
    private String userName;
    private String oldUserName;
    private String newUserName;
}

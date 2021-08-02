package com.samsung.bixby.mediastreaming.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Add user")
	void addUser(){
		UserRequestVO requestVO = UserRequestVO.builder()
												.userName("juicelee")
												.build();

	}

}

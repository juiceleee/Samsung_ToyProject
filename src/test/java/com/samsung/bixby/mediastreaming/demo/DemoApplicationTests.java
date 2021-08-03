package com.samsung.bixby.mediastreaming.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.bixby.mediastreaming.demo.dao.BasketRepository;
import com.samsung.bixby.mediastreaming.demo.dao.ItemRepository;
import com.samsung.bixby.mediastreaming.demo.dao.UserRepository;
import com.samsung.bixby.mediastreaming.demo.vo.BasketRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.ItemRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BasketRepository basketRepository;

	@BeforeEach
	public void initialize() throws Exception{
		itemRepository.deleteAll();
		userRepository.deleteAll();
		basketRepository.deleteAll();
	}

	@Test
	@DisplayName("Add user one")
	public void addUser() throws Exception{
		UserRequestVO requestVO = UserRequestVO.builder()
												.userName("juicelee")
												.build();
		ResultActions resultActions = this.mockMvc.perform(post("/shopping/user")
															.contentType(MediaType.APPLICATION_JSON)
															.content(objectMapper.writeValueAsString(requestVO))
															.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("juicelee").value(""));

	}

	@Test
	@DisplayName("Add same user")
	public void addSameUser() throws Exception{
		UserRequestVO requestVO = UserRequestVO.builder()
				.userName("juicelee")
				.build();
		ResultActions resultActions = this.mockMvc.perform(post("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("juicelee").value(""));

		resultActions = this.mockMvc.perform(post("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	@DisplayName("Add user and delete")
	public void deleteAndGetUser() throws Exception{
		UserRequestVO requestVO = UserRequestVO.builder()
				.userName("juicelee")
				.build();

		this.mockMvc.perform(post("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		ResultActions resultActions = this.mockMvc.perform(delete("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isNoContent());

		resultActions = this.mockMvc.perform(get("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("{}"));

	}

	@Test
	@DisplayName("Not sufficient item stock")
	public void itemNotSufficient() throws Exception{
		UserRequestVO requestVO = UserRequestVO.builder()
				.userName("juicelee")
				.build();

		this.mockMvc.perform(post("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));

		requestVO.setUserName("bwlee");

		this.mockMvc.perform(post("/shopping/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestVO))
				.accept(MediaType.APPLICATION_JSON));


		ResultActions resultActions = this.mockMvc.perform(get("/shopping/user")
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON));

		resultActions
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("juicelee").value(""))
				.andExpect(jsonPath("bwlee").value(""));

		ItemRequestVO itemRequestVO = ItemRequestVO.builder()
													.itemName("orange")
													.stock(10)
													.build();

		resultActions = this.mockMvc.perform(post("/shopping/item")
												.contentType(MediaType.APPLICATION_JSON)
												.content(objectMapper.writeValueAsString(itemRequestVO))
												.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isCreated())
					.andExpect(jsonPath("orange").value(10));

		BasketRequestVO basketRequestVO = BasketRequestVO.builder()
														.userName("juicelee")
														.itemName("orange")
														.itemCnt(6)
														.build();

		resultActions = this.mockMvc.perform(post("/shopping/basket")
												.contentType(MediaType.APPLICATION_JSON)
												.content(objectMapper.writeValueAsString(basketRequestVO))
												.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isCreated())
						.andExpect(jsonPath("orange").value(6));

		basketRequestVO.setUserName("bwlee");

		resultActions = this.mockMvc.perform(post("/shopping/basket")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(basketRequestVO))
				.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isBadRequest());



	}

}

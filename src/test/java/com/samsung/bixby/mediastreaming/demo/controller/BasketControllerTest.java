package com.samsung.bixby.mediastreaming.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.bixby.mediastreaming.demo.dao.entitiy.SellerEntity;
import com.samsung.bixby.mediastreaming.demo.vo.BasketRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.ItemRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.SellerRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class BasketControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BasketControllerTest(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void initialize() throws Exception {
    }

    public void addUser(String userName) throws Exception{
        UserRequestVO requestVO = UserRequestVO.builder()
                .userName(userName)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+userName).value(""));

    }

    public void addSeller(String sellerName) throws Exception{
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName(sellerName)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+sellerName).value(""));

    }

    public void addItem(String itemName, String sellerName, Integer stock) throws Exception{
        addSeller(sellerName);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName(itemName)
                .sellerName(sellerName)
                .stock(stock)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+itemName).value(stock.toString()));
    }

    private static Stream<Arguments> basketSource(){
        return Stream.of(
                arguments("testuser", "testitem", 5)
        );
    }



    @ParameterizedTest
    @MethodSource("basketSource")
    @DisplayName("Add one basket to user")
    @Transactional
    public void addBasket(String userName, String itemName, Integer itemCnt) throws Exception{
        addUser(userName);
        addItem(itemName, "testseller", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName(userName)
                .itemName(itemName)
                .itemCnt(itemCnt)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+itemName).value(itemCnt.toString()));

    }

    @ParameterizedTest
    @MethodSource("basketSource")
    @DisplayName("Add one basket to user that is not existing")
    @Transactional
    public void addBasketUserNotExisting(String userName, String itemName, Integer itemCnt) throws Exception{
        addUser(userName);
        addItem(itemName, "testseller", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser2")
                .itemName(itemName)
                .itemCnt(itemCnt)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @MethodSource("basketSource")
    @DisplayName("Add one basket to user when item is not existing")
    @Transactional
    public void addBasketItemNotExisting(String userName, String itemName, Integer itemCnt) throws Exception{
        addUser(userName);
        addItem(itemName, "testseller", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName(userName)
                .itemName("testitem2")
                .itemCnt(itemCnt)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Confirm buying")
    @Transactional
    public void confirmBuying() throws Exception{
        addBasket("testuser", "testitem", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser")
                .itemName("testitem")
                .itemCnt(5)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        ItemRequestVO itemRequestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .build();

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+"testitem").value("0"));
    }

    @Test
    @DisplayName("Confirm buying too much item")
    @Transactional
    public void confirmBuyingTooMuch() throws Exception{
        addBasket("testuser", "testitem", 6);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser")
                .itemName("testitem")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Confirm buying no user")
    @Transactional
    public void confirmBuyingNoUser() throws Exception{
        addBasket("testuser", "testitem", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser2")
                .itemName("testitem")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Confirm buying no item")
    @Transactional
    public void confirmBuyingNoItem() throws Exception{
        addBasket("testuser", "testitem", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser")
                .itemName("testitem2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete seller when item in basket")
    @Transactional
    public void deleteSellerInBasket() throws Exception{
        addBasket("testuser", "testitem", 5);

        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        BasketRequestVO basketRequestVO = BasketRequestVO.builder()
                .userName("testuser")
                .build();

        resultActions = this.mockMvc.perform(get("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basketRequestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{}"));
    }

    @Test
    @DisplayName("delete basket")
    @Transactional
    public void deleteBasket() throws Exception{
        addBasket("testuser", "testitem", 5);


        BasketRequestVO basketRequestVO = BasketRequestVO.builder()
                .userName("testuser")
                .itemName("testitem")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basketRequestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("Delete after confirm")
    @Transactional
    public void DeleteAfterConfirm() throws Exception{
        addBasket("testuser", "testitem", 5);

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser")
                .itemName("testitem")
                .itemCnt(5)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        resultActions = this.mockMvc.perform(get("/shopping/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{}"));
    }

    @Test
    @DisplayName("Confirm buying and check")
    @Transactional
    public void ConfirmAndCheck() throws Exception{
        confirmBuying();

        BasketRequestVO requestVO = BasketRequestVO.builder()
                .userName("testuser")
                .build();

        ResultActions resultActions = this.mockMvc.perform(get("/shopping/basket/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+"testitem").value("5"));
    }


}
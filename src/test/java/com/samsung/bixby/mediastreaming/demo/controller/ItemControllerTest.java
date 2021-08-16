package com.samsung.bixby.mediastreaming.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.bixby.mediastreaming.demo.vo.ItemRequestVO;
import com.samsung.bixby.mediastreaming.demo.vo.SellerRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ItemControllerTest(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
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

    @BeforeEach
    public void intialize() throws Exception{
    }

    private static Stream<Arguments> itemSource(){
        return Stream.of(
                arguments("testitem", "testseller", 5)
        );
    }

    @ParameterizedTest
    @MethodSource("itemSource")
    @DisplayName("Add one item")
    @Transactional
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

    @Test
    @DisplayName("delete one item")
    @Transactional
    public void deleteItem() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("delete not existing item")
    @Transactional
    public void deleteNotExistingItem() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem2")
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete item with wrong seller")
    @Transactional
    public void deleteItemWithWrongSeller() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update stock with plus value")
    @Transactional
    public void updateStockPlus() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .stock(6)
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+"testitem").value("11"));
    }

    @Test
    @DisplayName("update stock with minus value")
    @Transactional
    public void updateStockMinus() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .stock(-3)
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+"testitem").value("2"));
    }

    @Test
    @DisplayName("update stock with minus value results in zero")
    @Transactional
    public void updateStockDelete() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .stock(-5)
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{}"));
    }

    @Test
    @DisplayName("update stock with too much minus value results in zero")
    @Transactional
    public void updateStockTooMuch() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .stock(-6)
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("delete Seller and check item exists")
    @Transactional
    public void deleteSeller() throws Exception{
        addItem("testitem", "testseller", 5);

        SellerRequestVO sellerRequestVO = SellerRequestVO.builder()
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerRequestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .build();

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{}"));
    }

    @Test
    @DisplayName("delete Seller and check item exists")
    @Transactional
    public void deleteSellerWithItem() throws Exception{
        addItem("testitem", "testseller", 5);

        SellerRequestVO sellerRequestVO = SellerRequestVO.builder()
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellerRequestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem")
                .sellerName("testseller")
                .build();

        resultActions = this.mockMvc.perform(get("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{}"));
    }

    @Test
    @DisplayName("change item name")
    @Transactional
    public void changeItem() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .oldName("testitem")
                .newName("testitem2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("change not existing item's name")
    @Transactional
    public void changeNotExistingItem() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .oldName("testitem2")
                .newName("testitem3")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/item/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("change to existing item's name")
    @Transactional
    public void changeToExistingItem() throws Exception{
        addItem("testitem", "testseller", 5);

        ItemRequestVO requestVO = ItemRequestVO.builder()
                .itemName("testitem2")
                .sellerName("testseller")
                .stock(5)
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result."+"testitem2").value("5"));

        requestVO = ItemRequestVO.builder()
                .oldName("testitem")
                .newName("testitem2")
                .build();

        resultActions = this.mockMvc.perform(put("/shopping/item/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());
    }
}
package com.samsung.bixby.mediastreaming.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.bixby.mediastreaming.demo.vo.SellerRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SellerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public SellerControllerTest(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void initialize() throws Exception{
    }

    @ParameterizedTest
    @ValueSource(strings = {"testseller"})
    @DisplayName("Add one seller")
    @Transactional
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
                .andExpect(jsonPath(sellerName).value(""));

    }

    @Test
    @DisplayName("Add same seller")
    @Transactional
    public void addSameSeller() throws Exception{
        addSeller("testseller");
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("delete seller")
    @Transactional
    public void deleteSeller() throws Exception{
        addSeller("testseller");
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName("testseller")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("delete not existing seller")
    @Transactional
    public void deleteNotExistingSeller() throws Exception{
        addSeller("testseller");
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName("testseller2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("change seller name")
    @Transactional
    public void changeSeller() throws Exception{
        addSeller("testseller");
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .oldSellerName("testseller")
                .newSellerName("testseller2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("change name of not existing seller")
    @Transactional
    public void changeNotExistingSeller() throws Exception{
        addSeller("testseller");
        SellerRequestVO requestVO = SellerRequestVO.builder()
                .oldSellerName("testseller2")
                .newSellerName("testseller3")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("change name to existing seller")
    @Transactional
    public void changeToExistingSeller() throws Exception{
        addSeller("testseller");

        SellerRequestVO requestVO = SellerRequestVO.builder()
                .sellerName("testseller2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("testseller2").value(""));

        requestVO = SellerRequestVO.builder()
                .oldSellerName("testseller2")
                .newSellerName("testseller")
                .build();

        resultActions = this.mockMvc.perform(put("/shopping/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

}
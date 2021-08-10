package com.samsung.bixby.mediastreaming.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.samsung.bixby.mediastreaming.demo.vo.UserRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserServiceTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public UserServiceTest(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void initialize() throws Exception{
    }

    @ParameterizedTest
    @ValueSource(strings={"testuser"})
    @DisplayName("Add one user")
    @Transactional
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
                .andExpect(jsonPath(userName).value(""));

    }

    @Test
    @DisplayName("Add same user")
    @Transactional
    public void addSameUser() throws Exception{
        addUser("testuser");

        UserRequestVO requestVO = UserRequestVO.builder()
                .userName("testuser")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("delete user")
    @Transactional
    public void deleteUser() throws Exception{
        addUser("testuser");

        UserRequestVO requestVO = UserRequestVO.builder()
                .userName("testuser")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("delete not existing user")
    @Transactional
    public void deleteNotExistingUser() throws Exception{
        addUser("testuser");
        UserRequestVO requestVO = UserRequestVO.builder()
                .userName("testuser2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(delete("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("change user name")
    @Transactional
    public void changeUser() throws Exception{
        addUser("testuser");
        UserRequestVO requestVO = UserRequestVO.builder()
                .oldUserName("testuser")
                .newUserName("testuser2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("change name of not existing user")
    @Transactional
    public void changeNotExistingUser() throws Exception{
        addUser("testuser");
        UserRequestVO requestVO = UserRequestVO.builder()
                .oldUserName("testuser2")
                .newUserName("testuser3")
                .build();

        ResultActions resultActions = this.mockMvc.perform(put("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("change name to existing user")
    @Transactional
    public void changeToExistingUser() throws Exception{
        addUser("testuser");

        UserRequestVO requestVO = UserRequestVO.builder()
                .userName("testuser2")
                .build();

        ResultActions resultActions = this.mockMvc.perform(post("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("testuser2").value(""));

        requestVO = UserRequestVO.builder()
                .oldUserName("testuser2")
                .newUserName("testuser")
                .build();

        resultActions = this.mockMvc.perform(put("/shopping/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO))
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isBadRequest());

    }



}
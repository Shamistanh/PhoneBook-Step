package com.stepproject.phonebook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepproject.phonebook.dto.OperationDTO;
import com.stepproject.phonebook.dto.UserDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import com.stepproject.phonebook.service.impl.PhoneBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Qualifier("jacksonObjectMapper")
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private PhoneBookServiceImpl phoneBookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserController userController;


    private OperationDTO operationDTO = new OperationDTO();


    @Autowired
    private WebApplicationContext webApplicationContext;

    private UserDTO userDTO = new UserDTO();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();

        operationDTO = OperationDTO.builder()
                .userId(UUID.randomUUID())
                .operationStatus(OperationStatus.SUCCESS)
                .build();
         userDTO = UserDTO.builder()
                .name("Shamistan")
                .phone("Phone")
                .userId(UUID.randomUUID())
                .build();
    }
    @Test
    public void allUsersTest() throws Exception {
        when(phoneBookService.fetchAllUsers()).thenReturn(Arrays.asList(UserDTO.builder().build()));
        mockMvc.perform(get("/user/list")
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{'message':%s}", "success")));
        verify(phoneBookService, times(1)).fetchAllUsers();
    }

    @Test
    public void addUsersTest() throws Exception {
        operationDTO.setOperationType(OperationType.ADD);
        when(phoneBookService.addUser(any(UserDTO.class))).thenReturn(operationDTO);
        mockMvc.perform(post("/user/add")
                .contentType(APPLICATION_JSON)
                .content(givenObjectWhenConvertJsonThenSuccess(userDTO))
                .accept(ALL))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{'message':%s}", "success")));


        verify(phoneBookService, times(1)).addUser(any(UserDTO.class));
    }

    @Test
    public void editUsersTest() throws Exception {
        operationDTO.setOperationType(OperationType.EDIT);
        mockMvc.perform(put("/user/edit")
                .contentType(APPLICATION_JSON)
                .content(givenObjectWhenConvertJsonThenSuccess(userDTO))
                .accept(ALL))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{'message':%s}", "success")));


        verify(phoneBookService, times(1)).editUser(any(UserDTO.class));
    }

    @Test
    public void deleteUsersTest() throws Exception {
        operationDTO.setOperationType(OperationType.EDIT);
        mockMvc.perform(delete("/user/delete")
                .contentType(APPLICATION_JSON)
                .param("userId", UUID.randomUUID().toString())
                .accept(ALL))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{'message':%s}", "success")));


        verify(phoneBookService, times(1)).deleteUser(any(UUID.class));
    }
    private String givenObjectWhenConvertJsonThenSuccess(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }


}
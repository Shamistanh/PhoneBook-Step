package com.stepproject.phonebook.controller;


import com.stepproject.phonebook.dto.OperationDTO;
import com.stepproject.phonebook.dto.RestResponseDTO;
import com.stepproject.phonebook.dto.UserDTO;
import com.stepproject.phonebook.service.PhoneBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final PhoneBookService phoneBookService;

    private final String MESSAGE = "success";

    @GetMapping("/list")
    public RestResponseDTO<List<UserDTO>> getAllUsers(){
        return new RestResponseDTO<>(phoneBookService.fetchAllUsers(), MESSAGE);
    }

    @GetMapping("/status")
    public HttpStatus getStatus(){
        return HttpStatus.OK;
    }

    @PostMapping(value = "/add")
    public RestResponseDTO<OperationDTO> addUser(@RequestBody UserDTO UserDTO){
      return new RestResponseDTO<>(phoneBookService.addUser(UserDTO), MESSAGE);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseDTO<OperationDTO> editUser(@RequestBody UserDTO UserDTO){
        return new RestResponseDTO<>(phoneBookService.editUser(UserDTO), MESSAGE);
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseDTO<OperationDTO> deleteUser(@RequestParam UUID userId){
        return new RestResponseDTO<>(phoneBookService.deleteUser(userId), MESSAGE);
    }

}

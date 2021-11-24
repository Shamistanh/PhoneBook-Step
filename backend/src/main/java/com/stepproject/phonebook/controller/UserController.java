package com.stepproject.phonebook.controller;


import com.stepproject.phonebook.model.Operation;
import com.stepproject.phonebook.model.User;
import com.stepproject.phonebook.service.PhoneBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final PhoneBookService phoneBookService;

    @GetMapping("/list")
    public List<User> getAllUsers(){
        return phoneBookService.fetchAllUsers();
    }

    @GetMapping("/status")
    public HttpStatus getStatus(){
        return HttpStatus.OK;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Operation addUser(@RequestBody User user){
      return phoneBookService.addUser(user);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Operation editUser(@RequestBody User user){
        return phoneBookService.editUser(user);
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Operation deleteUser(@RequestParam UUID userId){
        return phoneBookService.deleteUser(userId);
    }

}

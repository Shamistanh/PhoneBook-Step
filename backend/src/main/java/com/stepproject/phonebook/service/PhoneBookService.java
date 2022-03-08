package com.stepproject.phonebook.service;


import com.stepproject.phonebook.dto.OperationDTO;
import com.stepproject.phonebook.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface PhoneBookService {

    List<UserDTO> fetchAllUsers();

    OperationDTO addUser(UserDTO userDTO);

    OperationDTO editUser(UserDTO userDTO);

    OperationDTO deleteUser(UUID userId);
}

package com.stepproject.phonebook.mapper;

import com.stepproject.phonebook.dto.OperationDTO;
import com.stepproject.phonebook.dto.UserDTO;
import com.stepproject.phonebook.model.Operation;
import com.stepproject.phonebook.model.User;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PhonebookMapper {

    UserDTO userToUserDto(User user);

    User userDTOToUser(UserDTO userDTO);

    OperationDTO operationToDTO(Operation operation);
}

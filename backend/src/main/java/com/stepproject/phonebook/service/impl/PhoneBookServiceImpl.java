package com.stepproject.phonebook.service.impl;

import com.stepproject.phonebook.dto.OperationDTO;
import com.stepproject.phonebook.dto.UserDTO;
import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import com.stepproject.phonebook.mapper.PhonebookMapper;
import com.stepproject.phonebook.mapper.PhonebookMapperImpl;
import com.stepproject.phonebook.model.Operation;
import com.stepproject.phonebook.model.User;
import com.stepproject.phonebook.repo.OperationRepository;
import com.stepproject.phonebook.repo.UserRepository;
import com.stepproject.phonebook.service.PhoneBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PhoneBookServiceImpl implements PhoneBookService {

    private final UserRepository userRepository;
    private PhonebookMapper phonebookMapper;
    private final OperationRepository operationRepository;

    public List<UserDTO> fetchAllUsers() {
        phonebookMapper = new PhonebookMapperImpl();
        return userRepository.findAll().stream()
                .map(phonebookMapper::userToUserDto)
                .collect(Collectors.toList());

    }

    public OperationDTO addUser(UserDTO userDTO) {
       phonebookMapper = new PhonebookMapperImpl();
        User user = phonebookMapper.userDTOToUser(userDTO);
        try {
            userRepository.save(user);
            Operation operation = new Operation();

            operation.setUserId(user.getUserId());
            operation.setOperationType(OperationType.ADD);
            operation.setOperationStatus(OperationStatus.SUCCESS);
            operationRepository.save(operation);
            return phonebookMapper.operationToDTO(operation);
        }catch (Exception exception){
            Operation operation = new Operation();

            operation.setUserId(user.getUserId());
            operation.setOperationType(OperationType.ADD);
            operation.setOperationStatus(OperationStatus.FAIL);
            operationRepository.save(operation);
            return phonebookMapper.operationToDTO(operation);
        }
    }

    public OperationDTO editUser(UserDTO userDTO) {
        phonebookMapper = new PhonebookMapperImpl();
        Operation operation = new Operation();

        Optional<User> userByIdOptional = userRepository.findById(userDTO.getUserId());

        if (userByIdOptional.isPresent()){
            User userDb = userByIdOptional.get();

            userDb.setPhone(userDTO.getPhone());
            userDb.setName(userDTO.getName());
            userRepository.save(userDb);

            operation.setOperationType(OperationType.EDIT);
            operation.setOperationStatus(OperationStatus.SUCCESS);
            operation.setUserId(userDTO.getUserId());
            operationRepository.save(operation);
            return phonebookMapper.operationToDTO(operation);
        }
        operation.setOperationType(OperationType.EDIT);
        operation.setOperationStatus(OperationStatus.FAIL);
        operationRepository.save(operation);
        return phonebookMapper.operationToDTO(operation);
    }

    public OperationDTO deleteUser(UUID userId) {
        phonebookMapper = new PhonebookMapperImpl();
        try {
            Operation operation = new Operation();

            Optional<User> userByIdOptional = userRepository.findById(userId);

            if (userByIdOptional.isPresent()) {
                User userDb = userByIdOptional.get();
                userRepository.delete(userDb);

                operation.setOperationType(OperationType.DELETE);
                operation.setOperationStatus(OperationStatus.SUCCESS);
                operation.setUserId(userId);
                operationRepository.save(operation);
                return phonebookMapper.operationToDTO(operation);
            }
            operation.setOperationType(OperationType.DELETE);
            operation.setOperationStatus(OperationStatus.FAIL);
            operationRepository.save(operation);
            return phonebookMapper.operationToDTO(operation);

        }catch (Exception exception){
            Operation operation = new Operation();
            operation.setOperationType(OperationType.DELETE);
            operation.setOperationStatus(OperationStatus.FAIL);
            operationRepository.save(operation);
            return phonebookMapper.operationToDTO(operation);
        }
    }
}

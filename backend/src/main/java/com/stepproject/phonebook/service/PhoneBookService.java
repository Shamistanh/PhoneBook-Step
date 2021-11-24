package com.stepproject.phonebook.service;

import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import com.stepproject.phonebook.model.Operation;
import com.stepproject.phonebook.model.User;
import com.stepproject.phonebook.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class PhoneBookService {

    private final UserRepository userRepository;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();

    }

    public Operation addUser(User user) {
        try {
            userRepository.save(user);
            Operation operation = new Operation();

            operation.setUserId(user.getUserId());
            operation.setOperationType(OperationType.ADD);
            operation.setOperationStatus(OperationStatus.SUCCESS);
            return operation;
        }catch (Exception exception){
            Operation operation = new Operation();

            operation.setUserId(user.getUserId());
            operation.setOperationType(OperationType.ADD);
            operation.setOperationStatus(OperationStatus.FAIL);
            return operation;
        }
    }

    public Operation editUser(User user) {
        Operation operation = new Operation();

        Optional<User> userByIdOptional = userRepository.findById(user.getUserId());

        if (userByIdOptional.isPresent()){
            User userDb = userByIdOptional.get();

            userDb.setPhone(user.getPhone());
            userDb.setName(user.getName());
            userRepository.save(userDb);

            operation.setOperationType(OperationType.EDIT);
            operation.setOperationStatus(OperationStatus.SUCCESS);
            operation.setUserId(user.getUserId());
            return operation;
        }
        operation.setOperationType(OperationType.EDIT);
        operation.setOperationStatus(OperationStatus.FAIL);
        return operation;
    }

    public Operation deleteUser(UUID userId) {

        try {
            Operation operation = new Operation();

            Optional<User> userByIdOptional = userRepository.findById(userId);

            if (userByIdOptional.isPresent()) {
                User userDb = userByIdOptional.get();
                userRepository.save(userDb);

                operation.setOperationType(OperationType.DEELETE);
                operation.setOperationStatus(OperationStatus.SUCCESS);
                operation.setUserId(userId);
                return operation;
            }
            operation.setOperationType(OperationType.DEELETE);
            operation.setOperationStatus(OperationStatus.FAIL);
            return operation;

        }catch (Exception exception){
            Operation operation = new Operation();
            operation.setOperationType(OperationType.DEELETE);
            operation.setOperationStatus(OperationStatus.FAIL);
            return operation;
        }
    }
}

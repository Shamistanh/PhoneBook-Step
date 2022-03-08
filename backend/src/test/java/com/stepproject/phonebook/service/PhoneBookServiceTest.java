package com.stepproject.phonebook.service;

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
import com.stepproject.phonebook.service.impl.PhoneBookServiceImpl;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhoneBookServiceTest {
    @InjectMocks
    PhoneBookServiceImpl phoneBookService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private PhonebookMapper phonebookMapper;

    @Mock
    private OperationRepository operationRepository;

    private List<User> users = new ArrayList<>();

    private User user;
    private UserDTO userDTO;

    private Operation operation = new Operation();

    @BeforeEach
    public void setUp() {
        UUID id = UUID.randomUUID();
        user = new User();
        user.setUserId(id);
        user.setName("name");
        users.add(user);

         userDTO = new UserDTO();
         userDTO.setUserId(id);
         userDTO.setName("name");

         operation.setOperationStatus(OperationStatus.SUCCESS);
         operation.setUserId(UUID.randomUUID());
    }

    @Test
    public void fetchAllUsers() {
        when(userRepository.findAll()).thenReturn(users);
        phoneBookService.fetchAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    public void addUser() {
        when(userRepository.save(user)).thenReturn(user);
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        OperationDTO operation = phoneBookService.addUser(userDTO);

        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.ADD);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.SUCCESS);
        Assertions.assertThat(operation.getUserId()).isEqualTo(user.getUserId());
        verify(operationRepository).save(any());
    }

    @Test
    public void addUserFailCaseTest() {
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        when(userRepository.save(user)).thenThrow(DuplicateKeyException.class);

        OperationDTO operation = phoneBookService.addUser(userDTO);

        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.ADD);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.FAIL);
        verify(operationRepository).save(any());
    }

    @Test
    public void editUser() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        OperationDTO operation = phoneBookService.editUser(userDTO);

        verify(userRepository).save(any());
        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.EDIT);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.SUCCESS);
        Assertions.assertThat(operation.getUserId()).isEqualTo(user.getUserId());
        verify(operationRepository).save(any());
    }

    @Test
    public void editUserWhenNoUserFound() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        OperationDTO operation = phoneBookService.editUser(userDTO);

        verify(userRepository, never()).save(any());
        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.EDIT)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));
        verify(operationRepository).save(any());
    }

    @Test
    public void deleteUser() {

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        OperationDTO operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.SUCCESS)));
        verify(operationRepository).save(any());
    }

    @Test
    public void deleteUserWhenUserNotFound() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        OperationDTO operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository, never()).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));
        verify(operationRepository).save(any());
    }

    @Test
    public void deleteUserWhenFailCase() {

        when(userRepository.findById(any())).thenThrow(DuplicateKeyException.class);

        OperationDTO operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository, never()).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));
        verify(operationRepository).save(any());
    }

}
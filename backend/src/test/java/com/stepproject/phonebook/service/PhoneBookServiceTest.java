package com.stepproject.phonebook.service;

import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import com.stepproject.phonebook.model.Operation;
import com.stepproject.phonebook.model.User;
import com.stepproject.phonebook.repo.UserRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
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
    PhoneBookService phoneBookService;

    @Mock
    private UserRepository userRepository;

    private List<User> users = new ArrayList<>();

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(UUID.randomUUID());
        user.setName("name");
        users.add(user);
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

        Operation operation = phoneBookService.addUser(user);

        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.ADD);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.SUCCESS);
        Assertions.assertThat(operation.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    public void addUserFailCaseTest() {
        when(userRepository.save(user)).thenThrow(DuplicateKeyException.class);

        Operation operation = phoneBookService.addUser(user);

        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.ADD);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.FAIL);
    }

    @Test
    public void editUser() {

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Operation operation = phoneBookService.editUser(user);

        verify(userRepository).save(any());
        Assertions.assertThat(operation.getOperationType()).isEqualTo(OperationType.EDIT);
        Assertions.assertThat(operation.getOperationStatus()).isEqualTo(OperationStatus.SUCCESS);
        Assertions.assertThat(operation.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    public void editUserWhenNoUserFound() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Operation operation = phoneBookService.editUser(user);

        verify(userRepository, never()).save(any());
        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.EDIT)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));
    }

    @Test
    public void deleteUser() {

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Operation operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.SUCCESS)));

    }

    @Test
    public void deleteUserWhenUserNotFound() {

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Operation operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository, never()).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));

    }

    @Test
    public void deleteUserWhenFailCase() {

        when(userRepository.findById(any())).thenThrow(DuplicateKeyException.class);

        Operation operation = phoneBookService.deleteUser(user.getUserId());
        verify(userRepository, never()).delete(any());

        MatcherAssert.assertThat(operation.getOperationType(), is(equalTo(OperationType.DELETE)));
        MatcherAssert.assertThat(operation.getOperationStatus(), is(equalTo(OperationStatus.FAIL)));

    }

}
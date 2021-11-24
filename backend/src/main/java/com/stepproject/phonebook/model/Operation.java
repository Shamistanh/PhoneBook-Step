package com.stepproject.phonebook.model;

import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Operation {

    @Id
    UUID userId;

    OperationType operationType;

    OperationStatus operationStatus;

}

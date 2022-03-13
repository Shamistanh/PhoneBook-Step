package com.stepproject.phonebook.dto;


import com.stepproject.phonebook.enums.OperationStatus;
import com.stepproject.phonebook.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class OperationDTO {

    private UUID userId;

    private OperationType operationType;

    private OperationStatus operationStatus;
}

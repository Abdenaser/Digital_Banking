package ma.enset.ebankbackend.DTOs;

import lombok.Data;
import ma.enset.ebankbackend.Entities.BankAccount;
import ma.enset.ebankbackend.Enums.OperationType;


import java.util.Date;

@Data
public class AccountOperationDTO {
    private  Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}

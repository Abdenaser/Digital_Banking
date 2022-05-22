package ma.enset.ebankbackend.DTOs;
import lombok.Data;
import ma.enset.ebankbackend.Enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private  double interestRate;
}

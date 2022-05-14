package ma.enset.ebankbackend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor @AllArgsConstructor @Data
public class SavingAccount extends BankAccount {
   private  double interestRate;
}

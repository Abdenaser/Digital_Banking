package ma.enset.ebankbackend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("SA") //SA = SavingAccount
@NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount {
   private  double interestRate;
}

package ma.enset.ebankbackend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CA") //CA = CurrentAccount
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrentAccount extends BankAccount{
    private double overDraft;
}

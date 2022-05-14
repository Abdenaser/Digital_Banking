package ma.enset.ebankbackend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrentAccount extends BankAccount{
    private double overDraft;
}

package ma.enset.ebankbackend.Repositories;

import ma.enset.ebankbackend.Entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}

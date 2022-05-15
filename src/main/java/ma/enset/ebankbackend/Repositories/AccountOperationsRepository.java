package ma.enset.ebankbackend.Repositories;

import ma.enset.ebankbackend.Entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationsRepository extends JpaRepository<AccountOperation,Long> {

}

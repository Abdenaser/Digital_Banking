package ma.enset.ebankbackend.Repositories;

import ma.enset.ebankbackend.Entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationsRepository extends JpaRepository<AccountOperation,Long> {
    public List<AccountOperation> findByBankAccountId(String accountId); // il faut faire la pagination
    Page<AccountOperation> findByBankAccountId(String account_Id, Pageable pageable);
}

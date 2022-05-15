package ma.enset.ebankbackend.Repositories;

import ma.enset.ebankbackend.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}

package ma.enset.ebankbackend.Web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankbackend.DTOs.CustomerDTO;
import ma.enset.ebankbackend.Entities.Customer;
import ma.enset.ebankbackend.Mappers.BankAccountMapperImpl;
import ma.enset.ebankbackend.Services.BankAccountServcie;
import ma.enset.ebankbackend.exceptions.CustomerNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import java.nio.file.Path;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class CustomerRestController {
    private BankAccountServcie bankAccountServcie;
    private BankAccountMapperImpl bankAccountMapper;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountServcie.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getcostomerdto(@PathVariable(name ="id") Long id_costomer) throws CustomerNotFoundException {
        return bankAccountServcie.getCostomer(id_costomer);
    }

    @PostMapping("/customers/add")
    public CustomerDTO addCustomerDTO(@RequestBody CustomerDTO customerDTO){
        return bankAccountServcie.saveCostomerDTO(customerDTO);
    }
    @PutMapping("/customers/{customer_id}")
    protected CustomerDTO updateCustomerDTO(@PathVariable(name = "customer_id") Long customer_id,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customer_id);
        return bankAccountServcie.updateCostomerDTO(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long id_customer){
        bankAccountServcie.deleteCustomer(id_customer);
    }
}

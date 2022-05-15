package ma.enset.ebankbackend;

import ma.enset.ebankbackend.Entities.AccountOperation;
import ma.enset.ebankbackend.Entities.CurrentAccount;
import ma.enset.ebankbackend.Entities.Customer;
import ma.enset.ebankbackend.Entities.SavingAccount;
import ma.enset.ebankbackend.Enums.AccountStatus;
import ma.enset.ebankbackend.Enums.OperationType;
import ma.enset.ebankbackend.Repositories.AccountOperationsRepository;
import ma.enset.ebankbackend.Repositories.BankAccountRepository;
import ma.enset.ebankbackend.Repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }
    @Bean
    //on test le model
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationsRepository accountOperationsRepository){
        return args ->{
            Stream.of("Abdenasser","Ali","Aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setBalance(Math.random()*20527);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(5820);
                currentAccount.setId(UUID.randomUUID().toString());//generateur d(ID) UUID
                currentAccount.setCreatedAt(new Date());
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setBalance(Math.random()*20527);
                savingAccount.setId(UUID.randomUUID().toString());//uuid genere des IDs depend de la date system donc toujour unique  !! mongodb utilise UUID
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(7);
                savingAccount.setCreatedAt(new Date());
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i=0;i<10;i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*54210);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationsRepository.save(accountOperation);
                }
            });

        };
    }

}




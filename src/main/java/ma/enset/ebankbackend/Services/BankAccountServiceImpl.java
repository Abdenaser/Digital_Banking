package ma.enset.ebankbackend.Services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankbackend.Entities.*;
import ma.enset.ebankbackend.Enums.OperationType;
import ma.enset.ebankbackend.Repositories.AccountOperationsRepository;
import ma.enset.ebankbackend.Repositories.BankAccountRepository;
import ma.enset.ebankbackend.Repositories.CustomerRepository;
import ma.enset.ebankbackend.exceptions.BalanceNotSufficientException;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankbackend.exceptions.CustomerNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@AllArgsConstructor //mieux que @Autowired
@Transactional //Attention il faut utiliser Transactional de Spring !!!
@Slf4j
//pour loger un  message c'est trés important d'utiliser un system de journalisation d'application et parmi les framework les plus connais dans java et log4j et l'un des API utiliser dans spring boot est : Slf4j egalement c'est un framwork qui fait la journalisation
public class BankAccountServiceImpl implements BankAccountServcie{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationsRepository accountOperationsRepository;
    public Customer saveCostomer(Customer customer) {
        log.info("saving new Customer");
        Customer savedCustomer=customerRepository.save(customer);
        return savedCustomer;
    }
//la creation d'un compte :
    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerid) throws CustomerNotFoundException {
        CurrentAccount currentAccount = new CurrentAccount();
        Customer customer=customerRepository.findById(customerid).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundException("Customer Not Found");
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount =bankAccountRepository.save(currentAccount);
        return savedBankAccount;

    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException {
        SavingAccount savingAccount=new SavingAccount();
        Customer customer=customerRepository.findById(customerid).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundException("Customer Not Found");
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saveSavingAccount =bankAccountRepository.save(savingAccount);
        return saveSavingAccount;
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException{
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
        return bankAccount;
    }

    @Override
    public void débit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=getBankAccount(accountId);
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperationsRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=getBankAccount(accountId);
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperationsRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        débit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource );

    }
}

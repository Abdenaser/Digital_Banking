package ma.enset.ebankbackend.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankbackend.DTOs.*;
import ma.enset.ebankbackend.Entities.*;
import ma.enset.ebankbackend.Enums.OperationType;
import ma.enset.ebankbackend.Mappers.BankAccountMapperImpl;
import ma.enset.ebankbackend.Repositories.AccountOperationsRepository;
import ma.enset.ebankbackend.Repositories.BankAccountRepository;
import ma.enset.ebankbackend.Repositories.CustomerRepository;
import ma.enset.ebankbackend.exceptions.BalanceNotSufficientException;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankbackend.exceptions.CustomerNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor //mieux que @Autowired
@Transactional //Attention il faut utiliser Transactional de Spring !!!
@Slf4j
//pour loger un  message c'est trés important d'utiliser un system de journalisation d'application et parmi les framework les plus connais dans java et log4j et l'un des API utiliser dans spring boot est : Slf4j egalement c'est un framwork qui fait la journalisation
public class BankAccountServiceImpl implements BankAccountServcie{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationsRepository accountOperationsRepository;
    private BankAccountMapperImpl bankAccountMapper;


    @Override
    public CustomerDTO saveCostomerDTO(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        CustomerDTO savedcustomerDTO=bankAccountMapper.fromCustomer(savedCustomer);
        return savedcustomerDTO;
    }

    //la creation d'un compte :
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerid) throws CustomerNotFoundException {
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
        return bankAccountMapper.fromCurrentBankAccount(savedBankAccount);

    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException {
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
        return bankAccountMapper.fromSavingBankAccount(saveSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();

        //la programmation impératif
        /**List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer :customers) {
            CustomerDTO customerDTO=bankAccountMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }**/
        // la programation fonctionel
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> listBankAccountDTO() {
        List<BankAccount> allbanksAccount = bankAccountRepository.findAll();
        List<BankAccountDTO> ban = allbanksAccount.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return ban;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException{
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void débit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
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
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
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

    @Override
    public CustomerDTO getCostomer(long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found !"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO updateCostomerDTO(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        CustomerDTO savedcustomerDTO=bankAccountMapper.fromCustomer(savedCustomer);
        return savedcustomerDTO;
    }

    @Override
    public List<AccountOperationDTO> accounthistorique(String account_id){
        List<AccountOperation> accountOperations = accountOperationsRepository.findByBankAccountId(account_id);
        return accountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation))
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id_account, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(id_account).orElseThrow(()->new BankAccountNotFoundException("not founded"));
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not found");
        Page<AccountOperation> accountoperations = accountOperationsRepository.findByBankAccountId(id_account, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountoperationsDTOS = accountoperations.stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountoperationsDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountoperations.getTotalPages());
        return accountHistoryDTO;
    }




}

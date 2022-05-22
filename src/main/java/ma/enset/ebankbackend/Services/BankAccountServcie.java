package ma.enset.ebankbackend.Services;

import ma.enset.ebankbackend.DTOs.*;
import ma.enset.ebankbackend.Entities.BankAccount;
import ma.enset.ebankbackend.Entities.CurrentAccount;
import ma.enset.ebankbackend.Entities.Customer;
import ma.enset.ebankbackend.Entities.SavingAccount;
import ma.enset.ebankbackend.exceptions.BalanceNotSufficientException;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountServcie {
    public CustomerDTO saveCostomerDTO(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft , Long costomerid) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate , Long costomerid) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> listBankAccountDTO();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void débit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    //initialbalance == sold initial
    CustomerDTO getCostomer(long id) throws CustomerNotFoundException;

    void deleteCustomer(Long id);

    CustomerDTO updateCostomerDTO(CustomerDTO customerDTO);


    List<AccountOperationDTO> accounthistorique(String account_id);

    AccountHistoryDTO getAccountHistory(String id_account, int page, int size) throws BankAccountNotFoundException;
}

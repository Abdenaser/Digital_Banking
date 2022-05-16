package ma.enset.ebankbackend.Services;

import ma.enset.ebankbackend.Entities.BankAccount;
import ma.enset.ebankbackend.Entities.CurrentAccount;
import ma.enset.ebankbackend.Entities.Customer;
import ma.enset.ebankbackend.Entities.SavingAccount;
import ma.enset.ebankbackend.exceptions.BalanceNotSufficientException;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.enset.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountServcie {
    public Customer saveCostomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft , Long costomerid) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate , Long costomerid) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void débit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    //initialbalance == sold initial
}

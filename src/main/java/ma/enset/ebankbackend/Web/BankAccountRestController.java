package ma.enset.ebankbackend.Web;

import lombok.AllArgsConstructor;
import ma.enset.ebankbackend.DTOs.AccountHistoryDTO;
import ma.enset.ebankbackend.DTOs.AccountOperationDTO;
import ma.enset.ebankbackend.DTOs.BankAccountDTO;
import ma.enset.ebankbackend.Services.BankAccountServcie;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountServcie bankAccountServcie;

    @GetMapping("/accounts/{account_id}")
    public BankAccountDTO getBankAccount(@PathVariable(name = "account_id") String account_id) throws BankAccountNotFoundException {
        return bankAccountServcie.getBankAccount(account_id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccountDTOList(){
        return bankAccountServcie.listBankAccountDTO();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> gethistory(@PathVariable(name = "id") String id_account){
        return bankAccountServcie.accounthistorique(id_account);
    }

    /*
    * pagination
    * */
    @GetMapping("/accounts/{id}/pageoperations")
    public AccountHistoryDTO getAccounthistory(
            @PathVariable(name = "id") String id_account,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name= "size",defaultValue = "4") int size) throws BankAccountNotFoundException {
        return bankAccountServcie.getAccountHistory(id_account,page,size);
    }
}

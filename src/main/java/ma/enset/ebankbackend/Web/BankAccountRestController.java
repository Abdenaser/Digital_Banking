package ma.enset.ebankbackend.Web;

import lombok.AllArgsConstructor;
import ma.enset.ebankbackend.DTOs.BankAccountDTO;
import ma.enset.ebankbackend.Entities.BankAccount;
import ma.enset.ebankbackend.Services.BankAccountServcie;
import ma.enset.ebankbackend.exceptions.BankAccountNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountServcie bankAccountServcie;

    @GetMapping("/accounts/{account_id}")
    public BankAccountDTO getBankAccount(String account_id) throws BankAccountNotFoundException {
        return bankAccountServcie.getBankAccount(account_id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccountDTOList(){
        return bankAccountServcie.listBankAccountDTO();
    }
}

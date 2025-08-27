package com.mourad.ebankingbackend.mappers;

import com.mourad.ebankingbackend.dtos.AccountOperationDTO;
import com.mourad.ebankingbackend.dtos.CurrentBankAcountDTO;
import com.mourad.ebankingbackend.dtos.CustomerDTO;
import com.mourad.ebankingbackend.dtos.SavingBankAcountDTO;
import com.mourad.ebankingbackend.enteties.AccountOperation;
import com.mourad.ebankingbackend.enteties.CurrentAccount;
import com.mourad.ebankingbackend.enteties.Customer;
import com.mourad.ebankingbackend.enteties.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer) {
       CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
       //customerDTO.setId(customer.getId());
       //customerDTO.setName(customer.getName());
      // customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAcountDTO savingBankAcountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAcountDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAcountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public SavingBankAcountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAcountDTO savingBankAcountDTO = new SavingBankAcountDTO();
        BeanUtils.copyProperties(savingAccount, savingBankAcountDTO);
        savingBankAcountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAcountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAcountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAcountDTO CurrentBankAcountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(CurrentBankAcountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(CurrentBankAcountDTO.getCustomerDTO()));
        return currentAccount;
    }
    public CurrentBankAcountDTO fromCurrentBankAcount(CurrentAccount currentAccount) {
    CurrentBankAcountDTO currentBankAcountDTO = new CurrentBankAcountDTO();
    BeanUtils.copyProperties(currentAccount, currentBankAcountDTO);
    currentBankAcountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAcountDTO.setType(currentAccount.getClass().getSimpleName());
    return currentBankAcountDTO;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;

    }
}

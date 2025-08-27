package com.mourad.ebankingbackend.services;

import com.mourad.ebankingbackend.dtos.*;
import com.mourad.ebankingbackend.enteties.*;
import com.mourad.ebankingbackend.enums.OperationType;
import com.mourad.ebankingbackend.exceptions.BalanceNotSufficentException;
import com.mourad.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.mourad.ebankingbackend.exceptions.CustomerNotFoundException;
import com.mourad.ebankingbackend.mappers.BankAccountMapperImpl;
import com.mourad.ebankingbackend.repositories.AccountOperationRepository;
import com.mourad.ebankingbackend.repositories.BankAccountRepository;
import com.mourad.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
   private BankAccountRepository bankAccountRepository;
   private CustomerRepository customerRepository;
   private AccountOperationRepository accountOperationRepository;
   private BankAccountMapperImpl dtoMapper;
   // Logger log= LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAcountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentBankAcount(savedBankAccount);
    }

    @Override
    public SavingBankAcountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
       SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> ListCustomers() {
        List<Customer> customers=customerRepository.findAll();
        List<CustomerDTO> customerDTOs = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
       /* List<CustomerDTO> customerDTOs=new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOs.add(customerDTO);
        }*/
        return customerDTOs;
    }


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        if(bankAcount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAcount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else{
            CurrentAccount currentAccount=(CurrentAccount) bankAcount;
            return dtoMapper.fromCurrentBankAcount(currentAccount);
        }
    }



    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException{
        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
    if(bankAcount.getBalance()<amount)
        throw new BalanceNotSufficentException("Balance not sufficient");
    AccountOperation accountOperation=new AccountOperation();
    accountOperation.setType(OperationType.DEBIT);
    accountOperation.setAmount(amount);
    accountOperation.setDescription(description);
    accountOperation.setOperationDate(new Date());
    accountOperation.setBankAccount(bankAcount);
    accountOperationRepository.save(accountOperation);
    bankAcount.setBalance(bankAcount.getBalance()-amount);
    bankAccountRepository.save(bankAcount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAcount bankAcount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAcount);
        accountOperationRepository.save(accountOperation);
        bankAcount.setBalance(bankAcount.getBalance()+amount);
        bankAccountRepository.save(bankAcount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAcount> bankAcounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAcounts.stream().map(bankAcount -> {
            if (bankAcount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAcount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAcount;
                return dtoMapper.fromCurrentBankAcount(currentAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer=customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accoutHistory(String accountId) throws BankAccountNotFoundException {
        List<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId);
       return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAcount bankAcount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAcount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS= accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAcount.getId());
        accountHistoryDTO.setBalance(bankAcount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;

    }

    @Override
    public List<CustomerDTO> searchCustumers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
            return customerDTOS;
        }



}

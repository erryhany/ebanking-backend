package com.mourad.ebankingbackend;

import com.mourad.ebankingbackend.dtos.BankAccountDTO;
import com.mourad.ebankingbackend.dtos.CurrentBankAcountDTO;
import com.mourad.ebankingbackend.dtos.CustomerDTO;
import com.mourad.ebankingbackend.dtos.SavingBankAcountDTO;
import com.mourad.ebankingbackend.enteties.*;
import com.mourad.ebankingbackend.enums.AccountStatus;
import com.mourad.ebankingbackend.enums.OperationType;
import com.mourad.ebankingbackend.exceptions.BalanceNotSufficentException;
import com.mourad.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.mourad.ebankingbackend.exceptions.CustomerNotFoundException;
import com.mourad.ebankingbackend.repositories.AccountOperationRepository;
import com.mourad.ebankingbackend.repositories.BankAccountRepository;
import com.mourad.ebankingbackend.repositories.CustomerRepository;
import com.mourad.ebankingbackend.services.BankAccountService;
import com.mourad.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {

		return args -> {
			Stream.of("Hassan","Imane","Mohammed").forEach(name -> {
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setName(name);
				customerDTO.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customerDTO);
			});
			bankAccountService.ListCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*12000,5.5, customer.getId());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
			List<BankAccountDTO> bankAcounts=bankAccountService.bankAccountList();
			for(BankAccountDTO bankAccount:bankAcounts){
				String accountId;
				for (int i = 0; i < 10; i++) {
					if(bankAccount instanceof CurrentBankAcountDTO){
						accountId=((CurrentBankAcountDTO) bankAccount).getId();
					} else {
						accountId=((SavingBankAcountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000+Math.random()*12000,"Credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");

				}
			}
		};
	}

	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args ->{
			Stream.of("Hassan","Yassine","Aicha").forEach(name->{
				Customer customer= new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust->{
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);


					SavingAccount savingAccount = new SavingAccount();
					savingAccount.setId(UUID.randomUUID().toString());
					savingAccount.setBalance(Math.random()*90000);
					savingAccount.setCreatedAt(new Date());
					savingAccount.setStatus(AccountStatus.CREATED);
					savingAccount.setCustomer(cust);
					savingAccount.setInterestRate(9000);
					bankAccountRepository.save(savingAccount);

				});
			bankAccountRepository.findAll().forEach(acc->{
				for(int i=0;i<10; i++){
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);


				}


			});
		};
	}

}

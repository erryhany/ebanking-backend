package com.mourad.ebankingbackend.services;

import com.mourad.ebankingbackend.enteties.BankAcount;
import com.mourad.ebankingbackend.enteties.CurrentAccount;
import com.mourad.ebankingbackend.enteties.SavingAccount;
import com.mourad.ebankingbackend.repositories.BankAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAcount bankAcount =
                bankAccountRepository.findById("ec49c791-cea1-4d44-8894-8131441ccc9c").orElse(null);
        System.out.println("************************");
        System.out.println(bankAcount.getId());
        System.out.println(bankAcount.getBalance());
        System.out.println(bankAcount.getStatus());
        System.out.println(bankAcount.getCreatedAt());
        System.out.println(bankAcount.getCustomer().getName());
        if(bankAcount instanceof CurrentAccount) {
            System.out.println("Over Draft => "+((CurrentAccount) bankAcount).getOverDraft());
        } else if(bankAcount instanceof SavingAccount) {
            System.out.println("Rate => "+((SavingAccount) bankAcount).getInterestRate());

        }
        bankAcount.getAccountOperations().forEach(op->{
            System.out.println(op.getType()+"\t"+op.getOperationDate()+"\t"+op.getAmount());
        });
    }
}

package com.mourad.ebankingbackend.dtos;


import com.mourad.ebankingbackend.enums.AccountStatus;
import lombok.Data;
import java.util.Date;


@Data
public class SavingBankAcountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interesRate;


}

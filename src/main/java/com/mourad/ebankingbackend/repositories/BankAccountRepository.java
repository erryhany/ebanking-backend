package com.mourad.ebankingbackend.repositories;

import com.mourad.ebankingbackend.enteties.BankAcount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAcount,String> {
}

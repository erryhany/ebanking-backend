package com.mourad.ebankingbackend.services;

import com.mourad.ebankingbackend.enteties.AccountOperation;
import com.mourad.ebankingbackend.enteties.CurrentAccount;
import com.mourad.ebankingbackend.enteties.SavingAccount;
import com.mourad.ebankingbackend.enums.OperationType;
import com.mourad.ebankingbackend.exceptions.BalanceNotSufficentException;
import com.mourad.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.mourad.ebankingbackend.mappers.BankAccountMapperImpl;
import com.mourad.ebankingbackend.repositories.AccountOperationRepository;
import com.mourad.ebankingbackend.repositories.BankAccountRepository;
import com.mourad.ebankingbackend.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountOperationRepository accountOperationRepository;
    @Mock
    private BankAccountMapperImpl dtoMapper;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private CurrentAccount currentAccount;
    private SavingAccount savingAccount;

    @BeforeEach
    void setUp() {
        currentAccount = new CurrentAccount();
        currentAccount.setId("CC-1");
        currentAccount.setBalance(1000);

        savingAccount = new SavingAccount();
        savingAccount.setId("SA-1");
        savingAccount.setBalance(500);
    }

    @Test
    void debit_shouldDecreaseBalanceAndSaveDebitOperation() throws Exception {
        when(bankAccountRepository.findById("CC-1")).thenReturn(Optional.of(currentAccount));

        bankAccountService.debit("CC-1", 300, "Retrait");

        assertThat(currentAccount.getBalance()).isEqualTo(700);
        ArgumentCaptor<AccountOperation> operation = ArgumentCaptor.forClass(AccountOperation.class);
        verify(accountOperationRepository).save(operation.capture());
        assertThat(operation.getValue().getType()).isEqualTo(OperationType.DEBIT);
        assertThat(operation.getValue().getAmount()).isEqualTo(300);
        assertThat(operation.getValue().getDescription()).isEqualTo("Retrait");
        assertThat(operation.getValue().getBankAccount()).isSameAs(currentAccount);
        verify(bankAccountRepository).save(currentAccount);
    }

    @Test
    void debit_shouldThrowWhenBalanceIsNotSufficient() {
        when(bankAccountRepository.findById("CC-1")).thenReturn(Optional.of(currentAccount));

        assertThatThrownBy(() -> bankAccountService.debit("CC-1", 1500, "Retrait"))
                .isInstanceOf(BalanceNotSufficentException.class);

        assertThat(currentAccount.getBalance()).isEqualTo(1000);
        verify(accountOperationRepository, never()).save(any());
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void debit_shouldThrowWhenAccountDoesNotExist() {
        when(bankAccountRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.debit("UNKNOWN", 100, "Retrait"))
                .isInstanceOf(BankAccountNotFoundException.class);

        verify(accountOperationRepository, never()).save(any());
    }

    @Test
    void credit_shouldIncreaseBalanceAndSaveCreditOperation() throws Exception {
        when(bankAccountRepository.findById("SA-1")).thenReturn(Optional.of(savingAccount));

        bankAccountService.credit("SA-1", 250, "Versement");

        assertThat(savingAccount.getBalance()).isEqualTo(750);
        ArgumentCaptor<AccountOperation> operation = ArgumentCaptor.forClass(AccountOperation.class);
        verify(accountOperationRepository).save(operation.capture());
        assertThat(operation.getValue().getType()).isEqualTo(OperationType.CREDIT);
        assertThat(operation.getValue().getAmount()).isEqualTo(250);
        verify(bankAccountRepository).save(savingAccount);
    }

    @Test
    void credit_shouldThrowWhenAccountDoesNotExist() {
        when(bankAccountRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.credit("UNKNOWN", 100, "Versement"))
                .isInstanceOf(BankAccountNotFoundException.class);

        verify(accountOperationRepository, never()).save(any());
    }

    @Test
    void transfer_shouldDebitSourceAndCreditDestination() throws Exception {
        when(bankAccountRepository.findById("CC-1")).thenReturn(Optional.of(currentAccount));
        when(bankAccountRepository.findById("SA-1")).thenReturn(Optional.of(savingAccount));

        bankAccountService.transfer("CC-1", "SA-1", 400);

        assertThat(currentAccount.getBalance()).isEqualTo(600);
        assertThat(savingAccount.getBalance()).isEqualTo(900);
        ArgumentCaptor<AccountOperation> operations = ArgumentCaptor.forClass(AccountOperation.class);
        verify(accountOperationRepository, times(2)).save(operations.capture());
        assertThat(operations.getAllValues())
                .extracting(AccountOperation::getType)
                .containsExactly(OperationType.DEBIT, OperationType.CREDIT);
        assertThat(operations.getAllValues().get(0).getDescription()).isEqualTo("Transfer to SA-1");
        assertThat(operations.getAllValues().get(1).getDescription()).isEqualTo("Transfer from CC-1");
    }

    @Test
    void transfer_shouldNotCreditDestinationWhenBalanceIsNotSufficient() {
        when(bankAccountRepository.findById("CC-1")).thenReturn(Optional.of(currentAccount));

        assertThatThrownBy(() -> bankAccountService.transfer("CC-1", "SA-1", 5000))
                .isInstanceOf(BalanceNotSufficentException.class);

        assertThat(currentAccount.getBalance()).isEqualTo(1000);
        assertThat(savingAccount.getBalance()).isEqualTo(500);
        verify(bankAccountRepository, never()).findById("SA-1");
        verify(accountOperationRepository, never()).save(any());
    }
}

package ru.senla.bank.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.senla.bank.consumer.adapters.db.BankRepositoryAdapter;
import ru.senla.bank.consumer.adapters.db.repository.AccountJpaRepository;
import ru.senla.bank.consumer.adapters.db.repository.TransferJpaRepository;
import ru.senla.bank.consumer.adapters.tx.TransactionManagerAdapter;
import ru.senla.bank.consumer.core.ports.BankRepositoryPort;
import ru.senla.bank.consumer.core.ports.TransactionPort;
import ru.senla.bank.consumer.core.service.TransferProcessingService;

@Configuration
public class AppConfig {

    @Bean
    public BankRepositoryPort bankRepositoryPort(
            AccountJpaRepository accountRepository,
            TransferJpaRepository transferRepository
    ) {
        return new BankRepositoryAdapter(accountRepository, transferRepository);
    }

    @Bean
    public TransferProcessingService transferProcessingService(
            BankRepositoryPort repository,
            TransactionPort transaction
    ) {
        return new TransferProcessingService(repository, transaction);
    }
}

package ru.senla.bank.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.senla.bank.producer.adapters.db.AccountRepositoryAdapter;
import ru.senla.bank.producer.adapters.db.repository.AccountJpaRepository;
import ru.senla.bank.producer.core.ports.AccountRepositoryPort;
import ru.senla.bank.producer.core.ports.TransferProducerPort;
import ru.senla.bank.producer.core.service.AccountInitializationService;
import ru.senla.bank.producer.core.service.TransferGenerationService;

@Configuration
public class AppConfig {

    @Bean
    public AccountRepositoryPort accountRepositoryPort(AccountJpaRepository repository) {
        return new AccountRepositoryAdapter(repository);
    }

    @Bean
    public AccountInitializationService accountInitializationService(AccountRepositoryPort port) {
        return new AccountInitializationService(port);
    }

    @Bean
    public TransferGenerationService transferGenerationService(
            TransferProducerPort producer,
            AccountInitializationService accountService
    ) {
        return new TransferGenerationService(producer, accountService);
    }
}

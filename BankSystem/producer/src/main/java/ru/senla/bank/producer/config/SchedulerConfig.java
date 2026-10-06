package ru.senla.bank.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.senla.bank.producer.adapters.scheduler.TransferScheduler;
import ru.senla.bank.producer.core.service.AccountInitializationService;
import ru.senla.bank.producer.core.service.TransferGenerationService;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public TransferScheduler transferScheduler(
            TransferGenerationService generationService,
            AccountInitializationService accountService
    ) {
        return new TransferScheduler(generationService, accountService);
    }
}

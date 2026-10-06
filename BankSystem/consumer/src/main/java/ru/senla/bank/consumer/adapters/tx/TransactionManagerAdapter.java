package ru.senla.bank.consumer.adapters.tx;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.senla.bank.consumer.core.ports.TransactionPort;

@Component
public class TransactionManagerAdapter implements TransactionPort {

    private final TransactionTemplate template;

    public TransactionManagerAdapter(PlatformTransactionManager txManager) {
        this.template = new TransactionTemplate(txManager);
    }

    @Override
    public void execute(Runnable action) {
        template.executeWithoutResult(status -> {
            try {
                action.run();
            } catch (RuntimeException e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}

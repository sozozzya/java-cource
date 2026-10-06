package ru.senla.bank.producer.adapters.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.senla.bank.producer.adapters.db.entity.AccountEntity;

import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
}

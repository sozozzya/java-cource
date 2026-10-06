package ru.senla.bank.consumer.adapters.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.senla.bank.consumer.adapters.db.entity.TransferEntity;

import java.util.UUID;

public interface TransferJpaRepository extends JpaRepository<TransferEntity, UUID> {
}

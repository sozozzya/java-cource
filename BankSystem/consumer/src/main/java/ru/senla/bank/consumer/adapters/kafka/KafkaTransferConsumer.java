package ru.senla.bank.consumer.adapters.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.senla.bank.common.model.TransferEvent;
import ru.senla.bank.consumer.core.service.TransferProcessingService;

import java.util.List;

@Component
public class KafkaTransferConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaTransferConsumer.class);

    private final TransferProcessingService service;

    public KafkaTransferConsumer(TransferProcessingService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = "transfers-topic",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            List<TransferEvent> events,
            Acknowledgment ack
    ) {
        log.info("Received batch of {} events", events.size());

        for (TransferEvent event : events) {
            log.info("Start processing: id={}", event.id());

            try {
                service.process(event);
            } catch (Exception e) {
                log.error("Unhandled error: id={}", event.id(), e);
            }
        }

        ack.acknowledge();
    }
}

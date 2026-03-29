package ru.senla.bank.producer.adapters.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import ru.senla.bank.common.model.TransferEvent;
import ru.senla.bank.producer.core.ports.TransferProducerPort;

import java.util.Objects;

public class KafkaTransferProducer implements TransferProducerPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaTransferProducer.class);

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;
    private final String topic;

    public KafkaTransferProducer(
            KafkaTemplate<String, TransferEvent> kafkaTemplate,
            String topic
    ) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate);
        this.topic = Objects.requireNonNull(topic);
    }

    @Override
    public void send(TransferEvent event) {
        try {
            kafkaTemplate.send(topic, event.id().toString(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send event: id={}", event.id(), ex);
                        } else {
                            log.debug("Sent event: id={}", event.id());
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to send event: id={}", event.id(), e);
            throw e;
        }
    }
}

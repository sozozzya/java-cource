package ru.senla.bank.producer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.senla.bank.common.model.TransferEvent;
import ru.senla.bank.common.serde.KafkaJsonSerializer;
import ru.senla.bank.producer.adapters.kafka.KafkaTransferProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, TransferEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:9092,kafka-2:9092,kafka-3:9092");

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        props.put(ProducerConfig.LINGER_MS_CONFIG, 50);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32_768);

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, TransferEvent> kafkaTemplate(
            ProducerFactory<String, TransferEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTransferProducer kafkaTransferProducer(
            KafkaTemplate<String, TransferEvent> kafkaTemplate
    ) {
        return new KafkaTransferProducer(kafkaTemplate, "transfers-topic");
    }
}

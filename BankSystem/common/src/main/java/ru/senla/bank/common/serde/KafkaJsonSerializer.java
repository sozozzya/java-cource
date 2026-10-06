package ru.senla.bank.common.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaJsonSerializer<T> implements Serializer<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Kafka message", e);
        }
    }
}

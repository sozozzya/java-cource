package ru.senla.bank.common.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import ru.senla.bank.common.model.TransferEvent;

public class TransferEventDeserializer implements Deserializer<TransferEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public TransferEvent deserialize(String topic, byte[] data) {
        if (data == null) return null;

        try {
            return OBJECT_MAPPER.readValue(data, TransferEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize", e);
        }
    }
}

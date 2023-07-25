package com.example.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaUtil {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private KafkaUtil(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    public void startListener() {
        log.info("Listener started ::  ");
        kafkaListenerEndpointRegistry.start();
    }

    public void startListener(Long zoneId) {
        log.info("Listener started :: for zone :: " + zoneId);
        kafkaListenerEndpointRegistry.getListenerContainer(String.valueOf(zoneId)).start();
    }

    public void stopListener() {
        kafkaListenerEndpointRegistry.stop(() -> {
            log.info("Listener Stopped.");
        });
    }

    public void stopListener(Long zoneId) {
        kafkaListenerEndpointRegistry.getListenerContainer(String.valueOf(zoneId)).stop(() -> {
            log.info("Listener Stopped :: for zone :: " + zoneId);
        });
    }
}

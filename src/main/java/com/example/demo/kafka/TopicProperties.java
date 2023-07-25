package com.example.demo.kafka;

import com.example.demo.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicProperties {
    @Autowired
    HomeService homeService;
    private final String id;

    private final String[] topics;

    public String getId() {
        return id;
    }

    public String[] getTopics() {
        return topics;
    }

    public TopicProperties(String id, String[] topics) {
        this.id = id;
        this.topics = topics;
    }


    @KafkaListener(id = "#{__listener.id}", topics = "#{__listener.topics}")
    public void listen(String message) {
        log.info("Listener started :: " + message);
        homeService.sendEvent(message);
    }
}

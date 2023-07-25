package com.example.demo.service;


import com.example.demo.dto.TopicAddDTO;
import reactor.core.publisher.Flux;

public interface HomeService {
    void sendEvent(String event);

    Flux<String> createConnection();
    Flux<String> createConnection(Long zoneId);

    String sendMessage(String message, String topic);

    String addTopic(TopicAddDTO addDTO, String zone);

    Object getCustomerByZoneId(Long zoneId);

    Object getCameraByZoneId(Long zoneId);
}

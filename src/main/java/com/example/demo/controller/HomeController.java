package com.example.demo.controller;

import com.example.demo.dto.TopicAddDTO;
import com.example.demo.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class HomeController {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createConnection() {
        log.info("HomeController  :: createConnection() :: called :: {}");
        return homeService.createConnection();
    }

    @GetMapping(path = "/sse/{zoneId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> createConnection(@PathVariable Long zoneId) {
        log.info("HomeController  :: createConnection() :: called :: {}");
        return homeService.createConnection(zoneId);
    }

    @PostMapping("/send/message")
    public String publishMessage(@RequestParam("message") String message, @RequestParam("topic") String topic) {
        log.info("HomeController  :: publishMessage() :: called :: {}");
        return homeService.sendMessage(message, topic);
    }

    @GetMapping("/dummy")
    public String getString() {
        return "server is up and running!";
    }

    @PostMapping("/add/topic/{zone}")
    public String addTopic(@RequestBody TopicAddDTO addDTO, @PathVariable String zone) {
        log.info("HomeController  :: addTopic() :: called :: {}");
        return homeService.addTopic(addDTO, zone);
    }
}

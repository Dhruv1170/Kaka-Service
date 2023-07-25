package com.example.demo.service.impl;


import com.example.demo.dto.CameraAccessor;
import com.example.demo.dto.CustomerAccessor;
import com.example.demo.dto.TopicAddDTO;
import com.example.demo.kafka.KafkaUtil;
import com.example.demo.kafka.TopicProperties;
import com.example.demo.repository.CameraRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final List<FluxSink<String>> emitters = new CopyOnWriteArrayList<>();

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final CustomerRepository customerRepository;

    private final KafkaUtil kafkaUtil;

    private final CameraRepository cameraRepository;
    @Autowired
    ApplicationContext applicationContext;

    public HomeServiceImpl(KafkaTemplate<String, String> kafkaTemplate, CustomerRepository customerRepository, KafkaUtil kafkaUtil, CameraRepository cameraRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.customerRepository = customerRepository;
        this.kafkaUtil = kafkaUtil;
        this.cameraRepository = cameraRepository;
    }

    @Override
    public void sendEvent(String event) {
        log.info("sending event :: " + event);
        List<FluxSink<String>> failedEmitters = new ArrayList<>();
        for (FluxSink<String> emitter : emitters) {
            try {
                emitter.next(event);
                System.out.println("even sent :: " + event);
            } catch (Exception e) {
                emitter.error(e);
                failedEmitters.add(emitter);
            }
        }
        emitters.removeAll(failedEmitters);
    }

    @Override
    public Flux<String> createConnection() {
        log.info("Creating connection :: {}");
        Flux<String> flux = Flux.create((FluxSink<String> emitter) -> {
            if (emitters.size() == 1) {
                kafkaUtil.startListener();
            }
            emitters.add(emitter);
            System.out.println("event added  :: " + emitters);
            emitter.onDispose(() -> {
                emitters.remove(emitter);
                System.out.println("Emitter disposed");
                if (emitters.isEmpty() || emitters.size() == 1) {
                    kafkaUtil.stopListener();
                }
            });
        });
        log.info("Connection created :: {}");
        return flux;
    }

    @Override
    public Flux<String> createConnection(Long zoneId) {
        log.info("Creating connection :: {}");
        Flux<String> flux = Flux.create((FluxSink<String> emitter) -> {
            if (emitters.size() == 1) {
                kafkaUtil.startListener(zoneId);
            }
            emitters.add(emitter);

            emitter.onDispose(() -> {
                emitters.remove(emitter);
                System.out.println("Emitter disposed");
                if (emitters.isEmpty() || emitters.size() == 1) {
                    kafkaUtil.stopListener(zoneId);
                }
            });
        });
        log.info("Connection created :: {}");
        return flux;
    }

    @Override
    public String sendMessage(String message, String topic) {
        try {
            log.info("kafka :: sendMessage() :: called ::  {}");
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("Exception while :: sendMessage() ::   " + e.getMessage());
        }
        return "Message published successfully!";
    }

    @Override
    public String addTopic(TopicAddDTO addDTO, String zone) {
        CustomerAccessor customerAccessor = getCustomerByZoneId(Long.valueOf(zone));
        List<CameraAccessor> cameraList = getCameraByZoneId(Long.valueOf(zone));
        List<String> topics = new ArrayList<>();
        for (CameraAccessor camera : cameraList) {
            String topic = customerAccessor.getId() + "_" + zone + "_" + camera.getCamId();
            topics.add(topic);
        }
        String[] arr = topics.toArray(new String[0]);
        TopicProperties topicProperties = new TopicProperties(zone, arr);
        applicationContext.getBean(TopicProperties.class, zone, arr);
        return "topic added successfully!";
    }

    @Override
    public CustomerAccessor getCustomerByZoneId(Long zoneId) {
        return customerRepository.getCustomerByZoneId(zoneId).orElseThrow(() -> new RuntimeException("Customer not found for zone :" + zoneId));
    }

    @Override
    public List<CameraAccessor> getCameraByZoneId(Long zoneId) {
        return cameraRepository.getCameraByZoneId(zoneId);
    }
}

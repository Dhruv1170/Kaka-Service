package com.example.demo;

import com.example.demo.kafka.TopicProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class RouterDataApplication {

    @Value("#{'${spring.kafka.topics}'.split(',')}")
    String[] topics;

    public static void main(String[] args) {
        SpringApplication.run(RouterDataApplication.class, args);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    TopicProperties topicProperties(String id, String[] topics) {
        return new TopicProperties(id, topics);
    }

    @Bean
    public ApplicationRunner runner(ApplicationContext context) {
        return args -> {
            context.getBean(TopicProperties.class, "t1", topics);
        };
    }
}
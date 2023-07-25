package com.example.demo.dto;

import java.util.List;

public class TopicAddDTO {

    List<String> topics;

    public TopicAddDTO() {
    }

    public TopicAddDTO(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}

package com.openclassrooms.mddapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public Topic create(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic getTopicByName(String name) {
        return topicRepository.findByName(name);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }
}

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

    /**
     * Permit to get topic by his id
     * @param id of the topic
     * @return topic
     */
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    /**
     * Permit to create topic
     * @param topic informations
     * @return topic
     */
    public Topic create(Topic topic) {
        return topicRepository.save(topic);
    }

    /**
     * Permit to get topic by his name
     * @param name of the topic
     * @return topic
     */
    public Topic getTopicByName(String name) {
        return topicRepository.findByName(name);
    }

    /**
     * Permit to get list of topics
     * @return list of topics
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }
}

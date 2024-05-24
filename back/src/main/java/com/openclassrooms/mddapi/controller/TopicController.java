package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.service.TopicService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    
    @Autowired
    private TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTopics(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            List<Topic> topics = topicService.getAllTopics();
            return ResponseEntity.ok(topics);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}

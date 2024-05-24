package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/topic")
public class TopicController {
    
    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public TopicController(TopicService topicService, UserService userService, JWTService jwtService) {
        this.topicService = topicService;
        this.userService = userService;
        this.jwtService = jwtService;
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

    @PutMapping("/subscribe")
    public ResponseEntity<?> subscribe(HttpServletRequest request, @RequestBody Topic topic) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String email = jwtService.decodeToken(token);
            User user = userService.getUser(email);

            List<Topic> currentTopicList = user.getTopics();
            currentTopicList.add(topic);
            user.setTopics(currentTopicList);
            userService.updateUser(user);
            
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}

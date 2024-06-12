package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
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

    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    /**
     * Endpoint to get all topics
     * @return list of topic
     */
    @GetMapping("")
    public ResponseEntity<?> getAllTopics() {
        List<Topic> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    /**
     * Endpoint to subscribe to a specific topic
     * @param request token for authentication
     * @param topic specific topic to subscribe
     * @return nothing
     */
    @PutMapping("/subscribe")
    public ResponseEntity<?> subscribe(HttpServletRequest request, @RequestBody Topic topic) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);
        List<Topic> currentTopicList = user.getTopics();
        currentTopicList.add(topic);
        user.setTopics(currentTopicList);
        userService.updateUser(user, false);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to unsubscribe to a specific topic
     * @param request token for authentication
     * @param topic specific topic to unsubscribe
     * @return nothing
     */
    @PutMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(HttpServletRequest request, @RequestBody Topic topic) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);
        List<Topic> currentTopicList = user.getTopics();
        currentTopicList.remove(topic);
        user.setTopics(currentTopicList);
        userService.updateUser(user, false);
        return ResponseEntity.ok().build();
    }
}

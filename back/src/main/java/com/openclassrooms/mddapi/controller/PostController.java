package com.openclassrooms.mddapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.TopicService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public PostController(PostService postService, TopicService topicService, UserService userService, JWTService jwtService) {
        this.postService = postService;
        this.topicService = topicService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(HttpServletRequest request, @RequestBody Post post) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String email = jwtService.decodeToken(token);
            User user = userService.getUser(email);
            
            if (post.getTopic() == null || post.getSubject().isEmpty() || post.getContent().isEmpty()) { // TODO: check empty fields in front
                return ResponseEntity.badRequest().body("Please fill all fields");
            } else if (topicService.getTopicById(post.getTopic().getId()) == null) {
                topicService.create(post.getTopic()); // TODO: check registration
            }
            Topic topic = topicService.getTopicByName(post.getTopic().getName());
            post.setTopic(topic);
            Post postRegistered = postService.create(post, user);
            return ResponseEntity.ok().body(postRegistered);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPostsSubscribe(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String email = jwtService.decodeToken(token);
            User user = userService.getUser(email);
            List<Post> posts = postService.getPostsSubscribe(user.getTopics());
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(HttpServletRequest request, @PathVariable("id") Long id) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            Post post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}

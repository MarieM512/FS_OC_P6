package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public PostController(PostRepository postRepository, TopicRepository topicRepository, UserService userService, JWTService jwtService) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(HttpServletRequest request, @RequestBody Post post) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String identifier = jwtService.decodeToken(token);
            User user = userService.getUser(identifier);
            
            if (post.getTopic() == null || post.getSubject().isEmpty() || post.getContent().isEmpty()) { // TODO: check empty fields in front
                return ResponseEntity.badRequest().body("Please fill all fields");
            } else if (topicRepository.findById(post.getTopic().getId()) == null) {
                topicRepository.save(post.getTopic()); // TODO: check registration
            }
            post.setUser(user);
            Post postRegistered = postRepository.save(post);
            return ResponseEntity.ok().body(postRegistered);
        } else {
            return ResponseEntity.status(401).build();
        }
        
    }
}

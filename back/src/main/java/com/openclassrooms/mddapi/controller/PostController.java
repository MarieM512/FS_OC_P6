package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;

@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicRepository topicRepository;

    public PostController(PostRepository postRepository, TopicRepository topicRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if (post.getTopic() == null || post.getSubject().isEmpty() || post.getContent().isEmpty()) { // TODO: check empty fields in front
            return ResponseEntity.badRequest().body("Please fill all fields");
        } else if (topicRepository.findById(post.getTopic().getId()) == null) {
            topicRepository.save(post.getTopic());
        }
        Post postRegistered = postRepository.save(post);
        return ResponseEntity.ok().body(postRegistered);
    }
}

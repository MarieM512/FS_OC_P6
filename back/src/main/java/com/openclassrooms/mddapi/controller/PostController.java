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
import com.openclassrooms.mddapi.model.dto.PostDTO;
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

    public PostController(PostService postService, TopicService topicService, UserService userService) {
        this.postService = postService;
        this.topicService = topicService;
        this.userService = userService;
    }

    /**
     * Endpoint to create a post
     * @param request token for authorisation
     * @param post information about the new post
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createPost(HttpServletRequest request, @RequestBody PostDTO post) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);

        if (post.getTopicId() == null || post.getSubject().isEmpty() || post.getContent().isEmpty()) {
            System.out.println(post);
            return ResponseEntity.badRequest().body("Please fill all fields");
        }
        Topic topic = topicService.getTopicById(post.getTopicId());
        Post postIntern = new Post();
        postIntern.setTopic(topic);
        postIntern.setUser(user);
        postIntern.setSubject(post.getSubject());
        postIntern.setContent(post.getContent());
        postService.create(postIntern);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to get all posts the user subscribed
     * @param request token for authorisation
     * @return list of posts
     */
    @GetMapping("")
    public ResponseEntity<List<Post>> getPostsSubscribe(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);
        List<Post> posts = postService.getPostsSubscribe(user.getTopics());
        return ResponseEntity.ok(posts);
    }

    /**
     * Endpoint to get a specific post by his id
     * @param id identification of the post
     * @return post
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
}

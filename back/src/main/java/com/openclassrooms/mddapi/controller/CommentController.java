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

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    public CommentController(CommentService commentService, PostService postService, UserService userService, JWTService jwtService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> create(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody String content) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String email = jwtService.decodeToken(token);
            User user = userService.getUser(email);
            Post post = postService.getPostById(id);
            commentService.create(content, user, post);
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(HttpServletRequest request, @PathVariable("id") Long id) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            List<Comment> comments = commentService.getCommentsFromPost(id);
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}

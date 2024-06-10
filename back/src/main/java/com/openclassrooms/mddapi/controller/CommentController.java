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

    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Endpoint to create a comment
     * @param request token for authorisation
     * @param id current post id
     * @param content content of comment
     * @return nothing
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> create(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody String content) {
        String email = request.getUserPrincipal().getName();
        User user = userService.getUser(email);
        Post post = postService.getPostById(id);
        commentService.create(content, user, post);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to get all comments for special post
     * @param id current post id
     * @return list of comments
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> getCommentById(@PathVariable("id") Long id) {
        List<Comment> comments = commentService.getCommentsFromPost(id);
        return ResponseEntity.ok(comments);
    }
}

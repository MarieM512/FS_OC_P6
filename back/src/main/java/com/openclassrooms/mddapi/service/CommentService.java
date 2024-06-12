package com.openclassrooms.mddapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    /**
     * Permit to create a comment
     * @param content of the commment
     * @param user that post the comment
     * @param post that link to the comment
     * @return comment
     */
    public Comment create(String content, User user, Post post) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    /**
     * Permit to get list of comments from a specific post
     * @param id of the post
     * @return list of comments
     */
    public List<Comment> getCommentsFromPost(Long id) {
        return commentRepository.findByPostId(id);
    }
}

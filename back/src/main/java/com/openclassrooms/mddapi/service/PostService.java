package com.openclassrooms.mddapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Permit to create a post
     * @param post to created
     * @return post has been created
     */
    public Post create(Post post) {
        return postRepository.save(post);
    }

    /**
     * Permit to get list of posts that the user subscribed
     * @param topicSub list of topics that the user subscribed
     * @return list of posts
     */
    public List<Post> getPostsSubscribe(List<Topic> topicSub) {
        List<Post> allPosts = postRepository.findAll();

        ArrayList<Long> topicIdSub = new ArrayList<>();
        for (int i = 0; i < topicSub.size(); i++) {
            topicIdSub.add(topicSub.get(i).getId());
        }

        ArrayList<Post> postSub = new ArrayList<>();
        for (int i = 0; i < allPosts.size(); i++) {
            for (int y = 0; y < topicIdSub.size(); y ++) {
                if (allPosts.get(i).getTopic().getId() == topicIdSub.get(y)) {
                    postSub.add(allPosts.get(i));
                }
            }
        }

        return postSub;
    }

    /**
     * Permit to get a specific post by his id
     * @param id of the post
     * @return post
     */
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}

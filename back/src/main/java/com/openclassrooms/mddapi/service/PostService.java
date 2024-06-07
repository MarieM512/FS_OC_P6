package com.openclassrooms.mddapi.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.dto.PostDTO;
import com.openclassrooms.mddapi.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public Post create(Post post) {
        // Post post = registerDtoToEntity(postDTO);
        // post.setTopic(topic);
        // post.setUser(user);
        return postRepository.save(post);
    }

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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

     private Post registerDtoToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
}

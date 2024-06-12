package com.openclassrooms.mddapi.model.dto;

import lombok.Data;

@Data
public class PostDTO {

    Long topicId;

    String subject;

    String content;
    
}
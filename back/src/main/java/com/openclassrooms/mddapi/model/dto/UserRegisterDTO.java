package com.openclassrooms.mddapi.model.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {

    private String username;

    private String email;

    private String password;
    
}

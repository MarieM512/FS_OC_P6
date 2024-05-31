package com.openclassrooms.mddapi.model.response;

import lombok.Data;

@Data
public class LogResponse {
    private String token;

    public LogResponse(String token) {
        this.token = token;
    }
}

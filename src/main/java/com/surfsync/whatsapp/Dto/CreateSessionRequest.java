package com.surfsync.whatsapp.Dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSessionRequest {

    @NotBlank(message = "Session name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
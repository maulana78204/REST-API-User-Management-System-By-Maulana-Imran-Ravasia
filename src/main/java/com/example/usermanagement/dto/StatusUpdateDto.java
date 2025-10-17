package com.example.usermanagement.dto;

import com.example.usermanagement.entity.StatusEnum;

import javax.validation.constraints.NotNull;

public class StatusUpdateDto {
    
    @NotNull(message = "Status tidak boleh kosong")
    private StatusEnum status;

    // Constructors
    public StatusUpdateDto() {}

    public StatusUpdateDto(StatusEnum status) {
        this.status = status;
    }

    // Getters and Setters
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
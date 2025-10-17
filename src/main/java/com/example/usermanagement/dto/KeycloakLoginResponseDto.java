package com.example.usermanagement.dto;

public class KeycloakLoginResponseDto {
    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private String message;

    // Constructors
    public KeycloakLoginResponseDto() {}

    public KeycloakLoginResponseDto(String accessToken, String tokenType, int expiresIn, String message) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.message = message;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

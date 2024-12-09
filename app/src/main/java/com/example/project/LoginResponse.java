package com.example.project;

public class LoginResponse {
    private String token;     // Token de autenticación
    private User user;        // Información del usuario

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

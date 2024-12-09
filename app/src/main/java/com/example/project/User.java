package com.example.project;

public class User {
    private String telefono;
    private String dni;
    private String name;
    private String email;

    // Constructor
    public User(String telefono, String dni, String name, String email) {
        this.telefono = telefono;
        this.dni = dni;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getTelefono() {
        return telefono;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setters (si los necesitas)
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

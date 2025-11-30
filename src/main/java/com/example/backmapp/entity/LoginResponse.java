package com.example.backmapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String rut;
    private String nombre;
    private String email;
    private String rol;
    private String estado;
}
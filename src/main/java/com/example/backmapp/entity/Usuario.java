package com.example.backmapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(length = 20)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    @Column(nullable = false)
    private RolUsuario  rol = RolUsuario.ADMINISTRADOR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    public enum EstadoUsuario {
        ACTIVO,
        INACTIVO,
        BLOQUEADO
    }

    public enum RolUsuario {
        ADMINISTRADOR,
        ARRENDATARIO,
        SUPERVISOR

    }
}

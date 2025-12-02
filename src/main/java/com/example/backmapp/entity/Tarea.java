package com.example.backmapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // disponibilidad semanal (veces que se puede tomar)
    @Column(nullable = false)
    private Integer disponibilidad;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false)
    private Integer puntos;

    @Column(length = 1000)
    private String reglas;

    // --- getters y setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Integer disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getReglas() {
        return reglas;
    }

    public void setReglas(String reglas) {
        this.reglas = reglas;
    }
}
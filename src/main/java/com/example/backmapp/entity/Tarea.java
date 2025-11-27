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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Min(value = 1, message = "La disponibilidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer disponibilidad;

    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    @Column(nullable = false)
    private Integer puntos;

    @Column(length = 1000)
    private String reglas;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionTarea> asignaciones = new ArrayList<>();
}

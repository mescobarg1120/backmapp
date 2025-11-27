package com.example.backmapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_tarea")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String usuarioNombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsignacion estado = EstadoAsignacion.TOMADA;

    @Column(length = 500)
    private String fotoConfirmacion;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion = LocalDateTime.now();

    private LocalDateTime fechaCompletada;

    private LocalDateTime fechaAprobada;

    @Column(length = 500)
    private String comentarioRechazo;

    public enum EstadoAsignacion {
        TOMADA,
        PENDIENTE_APROBACION,
        APROBADA,
        RECHAZADA
    }

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    }
}

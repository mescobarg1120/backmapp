package com.example.backmapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_tarea")
public class AsignacionTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario_rechazo", length = 500)
    private String comentarioRechazo;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia", nullable = false, length = 10)
    private DiaSemana dia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 25)
    private EstadoAsignacion estado;

    @Column(name = "fecha_aprobada")
    private LocalDateTime fechaAprobada;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;

    @Column(name = "foto_confirmacion", length = 500)
    private String fotoConfirmacion;

    @Column(name = "usuario_id", nullable = false, length = 255)
    private String usuarioId;

    @Column(name = "usuario_nombre", nullable = false, length = 255)
    private String usuarioNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;

    // -------- GETTERS & SETTERS --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarioRechazo() {
        return comentarioRechazo;
    }

    public void setComentarioRechazo(String comentarioRechazo) {
        this.comentarioRechazo = comentarioRechazo;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public EstadoAsignacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaAprobada() {
        return fechaAprobada;
    }

    public void setFechaAprobada(LocalDateTime fechaAprobada) {
        this.fechaAprobada = fechaAprobada;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    public String getFotoConfirmacion() {
        return fotoConfirmacion;
    }

    public void setFotoConfirmacion(String fotoConfirmacion) {
        this.fotoConfirmacion = fotoConfirmacion;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }
}
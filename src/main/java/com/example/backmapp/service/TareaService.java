package com.example.backmapp.service;

import com.example.backmapp.entity.AsignacionTarea;
import com.example.backmapp.entity.Tarea;
import com.example.backmapp.entity.DiaSemana;
import com.example.backmapp.entity.EstadoAsignacion;
import com.example.backmapp.repository.AsignacionTareaRepository;
import com.example.backmapp.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private AsignacionTareaRepository asignacionTareaRepository;

    // -------- CRUD BÁSICO DE TAREAS --------

    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea crear(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, Tarea tarea) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada");
        }
        tarea.setId(id);
        return tareaRepository.save(tarea);
    }

    public void eliminar(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada");
        }
        tareaRepository.deleteById(id);
    }

    // -------- LÓGICA DE NEGOCIO: ASIGNACIONES --------

    /**
     * Tomar una tarea para un día específico.
     */
    @Transactional
    public AsignacionTarea tomarTarea(Long tareaId, DiaSemana dia, String usuarioId, String usuarioNombre) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Verificar que no esté ya asignada ese día
        Optional<AsignacionTarea> asignacionExistente =
                asignacionTareaRepository.findByTarea_IdAndDia(tareaId, dia);

        if (asignacionExistente.isPresent()) {
            throw new RuntimeException("Esta tarea ya está asignada para este día");
        }

        // Verificar disponibilidad total (simple, sin separar por semana todavía)
        long asignacionesActuales = asignacionTareaRepository.findByTarea_Id(tareaId).size();
        if (asignacionesActuales >= tarea.getDisponibilidad()) {
            throw new RuntimeException("No hay disponibilidad para esta tarea");
        }

        AsignacionTarea asignacion = new AsignacionTarea();
        asignacion.setTarea(tarea);
        asignacion.setDia(dia);
        asignacion.setUsuarioId(usuarioId);
        asignacion.setUsuarioNombre(usuarioNombre);
        asignacion.setEstado(EstadoAsignacion.TOMADA);
        asignacion.setFechaAsignacion(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    /**
     * El usuario marca la tarea como completada y sube foto.
     */
    @Transactional
    public AsignacionTarea completarTarea(Long asignacionId, String fotoUri) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        asignacion.setEstado(EstadoAsignacion.PENDIENTE_APROBACION);
        asignacion.setFotoConfirmacion(fotoUri);
        asignacion.setFechaCompletada(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    /**
     * El admin aprueba una tarea completada.
     */
    @Transactional
    public AsignacionTarea aprobarTarea(Long asignacionId) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (asignacion.getEstado() != EstadoAsignacion.PENDIENTE_APROBACION) {
            throw new RuntimeException("La tarea no está pendiente de aprobación");
        }

        asignacion.setEstado(EstadoAsignacion.APROBADA);
        asignacion.setFechaAprobada(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    /**
     * El admin rechaza una tarea (por mala evidencia, fuera de plazo, etc.).
     */
    @Transactional
    public AsignacionTarea rechazarTarea(Long asignacionId, String comentario) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (asignacion.getEstado() != EstadoAsignacion.PENDIENTE_APROBACION) {
            throw new RuntimeException("La tarea no está pendiente de aprobación");
        }

        asignacion.setEstado(EstadoAsignacion.RECHAZADA);
        asignacion.setComentarioRechazo(comentario);
        asignacion.setFotoConfirmacion(null);

        return asignacionTareaRepository.save(asignacion);
    }

    /**
     * Tareas que están esperando revisión del admin.
     */
    public List<AsignacionTarea> obtenerPendientesAprobacion() {
        return asignacionTareaRepository.findByEstado(EstadoAsignacion.PENDIENTE_APROBACION);
    }

    /**
     * Liberar una asignación (por ejemplo, el admin la quita manualmente).
     */
    @Transactional
    public void liberarTarea(Long asignacionId) {
        if (!asignacionTareaRepository.existsById(asignacionId)) {
            throw new RuntimeException("Asignación no encontrada");
        }
        asignacionTareaRepository.deleteById(asignacionId);
    }
}
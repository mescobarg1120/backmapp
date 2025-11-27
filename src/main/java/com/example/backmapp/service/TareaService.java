package com.example.backmapp.service;

import com.example.backmapp.entity.AsignacionTarea;
import com.example.backmapp.entity.Tarea;
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

    @Transactional
    public AsignacionTarea tomarTarea(Long tareaId, AsignacionTarea.DiaSemana dia, String usuarioId, String usuarioNombre) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Verificar que no esté ya asignada ese día
        Optional<AsignacionTarea> asignacionExistente = asignacionTareaRepository.findByTareaIdAndDia(tareaId, dia);
        if (asignacionExistente.isPresent()) {
            throw new RuntimeException("Esta tarea ya está asignada para este día");
        }

        // Verificar disponibilidad
        long asignacionesActuales = asignacionTareaRepository.findByTareaId(tareaId).size();
        if (asignacionesActuales >= tarea.getDisponibilidad()) {
            throw new RuntimeException("No hay disponibilidad para esta tarea");
        }

        AsignacionTarea asignacion = new AsignacionTarea();
        asignacion.setTarea(tarea);
        asignacion.setDia(dia);
        asignacion.setUsuarioId(usuarioId);
        asignacion.setUsuarioNombre(usuarioNombre);
        asignacion.setEstado(AsignacionTarea.EstadoAsignacion.TOMADA);
        asignacion.setFechaAsignacion(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    @Transactional
    public AsignacionTarea completarTarea(Long asignacionId, String fotoUri) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        asignacion.setEstado(AsignacionTarea.EstadoAsignacion.PENDIENTE_APROBACION);
        asignacion.setFotoConfirmacion(fotoUri);
        asignacion.setFechaCompletada(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    @Transactional
    public AsignacionTarea aprobarTarea(Long asignacionId) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (asignacion.getEstado() != AsignacionTarea.EstadoAsignacion.PENDIENTE_APROBACION) {
            throw new RuntimeException("La tarea no está pendiente de aprobación");
        }

        asignacion.setEstado(AsignacionTarea.EstadoAsignacion.APROBADA);
        asignacion.setFechaAprobada(LocalDateTime.now());

        return asignacionTareaRepository.save(asignacion);
    }

    @Transactional
    public AsignacionTarea rechazarTarea(Long asignacionId, String comentario) {
        AsignacionTarea asignacion = asignacionTareaRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (asignacion.getEstado() != AsignacionTarea.EstadoAsignacion.PENDIENTE_APROBACION) {
            throw new RuntimeException("La tarea no está pendiente de aprobación");
        }

        asignacion.setEstado(AsignacionTarea.EstadoAsignacion.RECHAZADA);
        asignacion.setComentarioRechazo(comentario);
        asignacion.setFotoConfirmacion(null);

        return asignacionTareaRepository.save(asignacion);
    }

    public List<AsignacionTarea> obtenerPendientesAprobacion() {
        return asignacionTareaRepository.findByEstado(AsignacionTarea.EstadoAsignacion.PENDIENTE_APROBACION);
    }

    public void liberarTarea(Long asignacionId) {
        if (!asignacionTareaRepository.existsById(asignacionId)) {
            throw new RuntimeException("Asignación no encontrada");
        }
        asignacionTareaRepository.deleteById(asignacionId);
    }
}

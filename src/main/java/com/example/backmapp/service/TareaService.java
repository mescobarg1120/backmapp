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

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    // -------- ASIGNACIONES: CONSULTAS --------

    public List<AsignacionTarea> obtenerTodasAsignaciones() {
        return asignacionTareaRepository.findAll();
    }

    /**
     * Asignaciones para el admin, filtradas por rango de fechas (sobre fechaAsignacion).
     * Si no se envían fechas, se usa la semana actual (lunes a domingo).
     */
    public List<AsignacionTarea> obtenerAsignacionesAdmin(LocalDate desde, LocalDate hasta) {

        LocalDateTime desdeDT;
        LocalDateTime hastaDT;

        if (desde == null || hasta == null) {
            LocalDate hoy = LocalDate.now();
            LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
            LocalDate finSemana = inicioSemana.plusDays(6); // lunes + 6 = domingo

            desdeDT = inicioSemana.atStartOfDay();
            // usamos exclusivo al día siguiente
            hastaDT = finSemana.plusDays(1).atStartOfDay();
        } else {
            desdeDT = desde.atStartOfDay();
            hastaDT = hasta.plusDays(1).atStartOfDay();
        }

        return asignacionTareaRepository.findByFechaAsignacionBetween(desdeDT, hastaDT);
    }

    /**
     * Asignaciones de un usuario (para "Mis tareas" / dashboard).
     * Si no se envían fechas -> todas las asignaciones del usuario.
     * Si se envían fechas -> filtrado por rango.
     */
    public List<AsignacionTarea> obtenerAsignacionesUsuario(String usuarioId, LocalDate desde, LocalDate hasta) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new RuntimeException("usuarioId es obligatorio");
        }

        // sin rango -> todas las asignaciones del usuario
        if (desde == null && hasta == null) {
            return asignacionTareaRepository.findByUsuarioId(usuarioId);
        }

        // si solo viene 'desde' o solo 'hasta', completamos el otro extremo con algo razonable
        if (desde == null) {
            // hace 30 días por decir algo
            desde = LocalDate.now().minusDays(30);
        }
        if (hasta == null) {
            // hoy
            hasta = LocalDate.now();
        }

        LocalDateTime desdeDT = desde.atStartOfDay();
        LocalDateTime hastaDT = hasta.plusDays(1).atStartOfDay();

        return asignacionTareaRepository.findByUsuarioIdAndFechaAsignacionBetween(
                usuarioId, desdeDT, hastaDT
        );
    }

    // -------- LÓGICA DE NEGOCIO: ASIGNACIONES --------

    private boolean estaVencida(AsignacionTarea a) {
        return a.getEstado() == EstadoAsignacion.TOMADA &&
               a.getFechaAsignacion()
                .plusHours(36)
                .isBefore(LocalDateTime.now());
    }

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

        // Verificar disponibilidad total
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
     * El usuario marca la tarea como completada y sube foto (o texto con evidencia).
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
     * El admin rechaza una tarea (por mala evidencia, etc.).
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
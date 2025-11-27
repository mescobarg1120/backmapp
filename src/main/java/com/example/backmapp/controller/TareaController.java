package com.example.backmapp.controller;

import com.example.backmapp.entity.AsignacionTarea;
import com.example.backmapp.entity.Tarea;
import com.example.backmapp.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodas() {
        return ResponseEntity.ok(tareaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        return tareaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Tarea tarea) {
        try {
            Tarea nuevaTarea = tareaService.crear(tarea);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Tarea tarea) {
        try {
            Tarea tareaActualizada = tareaService.actualizar(id, tarea);
            return ResponseEntity.ok(tareaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/tomar")
    public ResponseEntity<?> tomarTarea(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            AsignacionTarea.DiaSemana dia = AsignacionTarea.DiaSemana.valueOf(request.get("dia"));
            String usuarioId = request.get("usuarioId");
            String usuarioNombre = request.get("usuarioNombre");

            AsignacionTarea asignacion = tareaService.tomarTarea(id, dia, usuarioId, usuarioNombre);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/asignaciones/{asignacionId}/completar")
    public ResponseEntity<?> completarTarea(
            @PathVariable Long asignacionId,
            @RequestBody Map<String, String> request) {
        try {
            String fotoUri = request.get("fotoUri");
            AsignacionTarea asignacion = tareaService.completarTarea(asignacionId, fotoUri);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/asignaciones/{asignacionId}/aprobar")
    public ResponseEntity<?> aprobarTarea(@PathVariable Long asignacionId) {
        try {
            AsignacionTarea asignacion = tareaService.aprobarTarea(asignacionId);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/asignaciones/{asignacionId}/rechazar")
    public ResponseEntity<?> rechazarTarea(
            @PathVariable Long asignacionId,
            @RequestBody Map<String, String> request) {
        try {
            String comentario = request.get("comentario");
            AsignacionTarea asignacion = tareaService.rechazarTarea(asignacionId, comentario);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendientes-aprobacion")
    public ResponseEntity<List<AsignacionTarea>> obtenerPendientesAprobacion() {
        return ResponseEntity.ok(tareaService.obtenerPendientesAprobacion());
    }

    @DeleteMapping("/asignaciones/{asignacionId}")
    public ResponseEntity<?> liberarTarea(@PathVariable Long asignacionId) {
        try {
            tareaService.liberarTarea(asignacionId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

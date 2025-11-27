package com.example.backmapp.controller;

import com.example.backmapp.entity.*;
import com.example.backmapp.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finanzas")
@CrossOrigin(origins = "*")
public class FinanzaController {

    @Autowired
    private FinanzaService finanzaService;

    @GetMapping
    public ResponseEntity<List<Finanza>> obtenerTodas() {
        return ResponseEntity.ok(finanzaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Finanza> obtenerPorId(@PathVariable Long id) {
        return finanzaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Finanza>> obtenerPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(finanzaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/gastos")
    public ResponseEntity<List<Finanza>> obtenerGastos() {
        return ResponseEntity.ok(finanzaService.obtenerGastos());
    }

    @GetMapping("/rango")
    public ResponseEntity<List<Finanza>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(finanzaService.obtenerPorRangoFechas(fechaInicio, fechaFin));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Finanza finanza) {
        try {
            Finanza nuevaFinanza = finanzaService.crear(finanza);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFinanza);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Finanza finanza) {
        try {
            Finanza finanzaActualizada = finanzaService.actualizar(id, finanza);
            return ResponseEntity.ok(finanzaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            finanzaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Double>> obtenerResumen() {
        Map<String, Double> resumen = new HashMap<>();
        resumen.put("totalIngresos", finanzaService.calcularTotalIngresos());
        resumen.put("totalGastos", finanzaService.calcularTotalGastos());
        resumen.put("saldo", finanzaService.calcularSaldo());
        return ResponseEntity.ok(resumen);
    }

    // Endpoints para UsuarioFinanza
    @GetMapping("/usuarios-finanza")
    public ResponseEntity<List<UsuarioFinanza>> obtenerTodosUsuariosFinanza() {
        return ResponseEntity.ok(finanzaService.obtenerTodosUsuariosFinanza());
    }

    @GetMapping("/usuarios-finanza/{usuarioId}")
    public ResponseEntity<UsuarioFinanza> obtenerUsuarioFinanzaPorId(@PathVariable String usuarioId) {
        return finanzaService.obtenerUsuarioFinanzaPorId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/usuarios-finanza")
    public ResponseEntity<UsuarioFinanza> crearUsuarioFinanza(@Valid @RequestBody UsuarioFinanza usuarioFinanza) {
        UsuarioFinanza nuevo = finanzaService.crearUsuarioFinanza(usuarioFinanza);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/usuarios-finanza/{usuarioId}")
    public ResponseEntity<?> actualizarUsuarioFinanza(
            @PathVariable String usuarioId,
            @Valid @RequestBody UsuarioFinanza usuarioFinanza) {
        try {
            UsuarioFinanza actualizado = finanzaService.actualizarUsuarioFinanza(usuarioId, usuarioFinanza);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

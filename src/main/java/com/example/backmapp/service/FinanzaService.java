package com.example.backmapp.service;

import com.example.backmapp.entity.Finanza;
import com.example.backmapp.entity.UsuarioFinanza;
import com.example.backmapp.repository.FinanzaRepository;
import com.example.backmapp.repository.UsuarioFinanzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FinanzaService {

    @Autowired
    private FinanzaRepository finanzaRepository;

    @Autowired
    private UsuarioFinanzaRepository usuarioFinanzaRepository;

    public List<Finanza> obtenerTodas() {
        return finanzaRepository.findAll();
    }

    public Optional<Finanza> obtenerPorId(Long id) {
        return finanzaRepository.findById(id);
    }

    public List<Finanza> obtenerPorUsuario(String usuarioId) {
        return finanzaRepository.findByUsuarioId(usuarioId);
    }

    public List<Finanza> obtenerGastos() {
        return finanzaRepository.findByTipo(Finanza.TipoFinanza.EGRESO);
    }

    public List<Finanza> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return finanzaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public Finanza crear(Finanza finanza) {
        return finanzaRepository.save(finanza);
    }

    public Finanza actualizar(Long id, Finanza finanza) {
        if (!finanzaRepository.existsById(id)) {
            throw new RuntimeException("Finanza no encontrada");
        }
        finanza.setId(id);
        return finanzaRepository.save(finanza);
    }

    public void eliminar(Long id) {
        if (!finanzaRepository.existsById(id)) {
            throw new RuntimeException("Finanza no encontrada");
        }
        finanzaRepository.deleteById(id);
    }

    public Double calcularTotalIngresos() {
        Double total = finanzaRepository.sumMontoByTipo(Finanza.TipoFinanza.INGRESO);
        return total != null ? total : 0.0;
    }

    public Double calcularTotalGastos() {
        Double total = finanzaRepository.sumMontoByTipo(Finanza.TipoFinanza.EGRESO);
        return total != null ? total : 0.0;
    }

    public Double calcularSaldo() {
        return calcularTotalIngresos() - calcularTotalGastos();
    }

    // Métodos para UsuarioFinanza
    public List<UsuarioFinanza> obtenerTodosUsuariosFinanza() {
        return usuarioFinanzaRepository.findAll();
    }

    public Optional<UsuarioFinanza> obtenerUsuarioFinanzaPorId(String usuarioId) {
        return usuarioFinanzaRepository.findById(usuarioId);
    }

    public UsuarioFinanza crearUsuarioFinanza(UsuarioFinanza usuarioFinanza) {
        return usuarioFinanzaRepository.save(usuarioFinanza);
    }

    public UsuarioFinanza actualizarUsuarioFinanza(String usuarioId, UsuarioFinanza usuarioFinanza) {
        if (!usuarioFinanzaRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario finanza no encontrado");
        }
        usuarioFinanza.setUsuarioId(usuarioId);
        return usuarioFinanzaRepository.save(usuarioFinanza);
    }
}

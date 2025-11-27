package com.example.backmapp.repository;

import com.example.backmapp.entity.Finanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanzaRepository extends JpaRepository<Finanza, Long> {

    List<Finanza> findByUsuarioId(String usuarioId);

    List<Finanza> findByTipo(Finanza.TipoFinanza tipo);

    List<Finanza> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT SUM(f.monto) FROM Finanza f WHERE f.tipo = :tipo")
    Double sumMontoByTipo(Finanza.TipoFinanza tipo);
}

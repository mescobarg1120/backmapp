package com.example.backmapp.repository;

import com.example.backmapp.entity.AsignacionTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionTareaRepository extends JpaRepository<AsignacionTarea, Long> {

    List<AsignacionTarea> findByTareaId(Long tareaId);

    List<AsignacionTarea> findByUsuarioId(String usuarioId);

    List<AsignacionTarea> findByEstado(AsignacionTarea.EstadoAsignacion estado);

    Optional<AsignacionTarea> findByTareaIdAndDia(Long tareaId, AsignacionTarea.DiaSemana dia);
}

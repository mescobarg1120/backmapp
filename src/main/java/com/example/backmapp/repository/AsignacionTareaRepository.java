package com.example.backmapp.repository;

import com.example.backmapp.entity.AsignacionTarea;
import com.example.backmapp.entity.DiaSemana;
import com.example.backmapp.entity.EstadoAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
        
import java.util.List;
import java.util.Optional;


public interface AsignacionTareaRepository extends JpaRepository<AsignacionTarea, Long> {

    // Buscar si una tarea ya está asignada un día específico
    Optional<AsignacionTarea> findByTarea_IdAndDia(Long tareaId, DiaSemana dia);

    // Listar asignaciones de una tarea (sirve para contar cuántas veces se ha tomado)
    List<AsignacionTarea> findByTarea_Id(Long tareaId);

    // Listar asignaciones por estado (por ejemplo, pendientes de aprobación)
    List<AsignacionTarea> findByEstado(EstadoAsignacion estado);
}
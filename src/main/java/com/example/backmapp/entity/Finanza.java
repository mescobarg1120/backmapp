package com.example.backmapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "finanzas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Finanza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El ID de usuario es obligatorio")
    @Column(nullable = false)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFinanza tipo = TipoFinanza.EGRESO;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto no puede ser negativo")
    @Column(nullable = false)
    private Double monto;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String categoria;

    public enum TipoFinanza {
        INGRESO,
        EGRESO
    }
}

package com.example.backmapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_finanzas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFinanza {

    @Id
    @Column(length = 20)
    private String usuarioId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer puntos = 0;

    @Column(nullable = false)
    private Double totalAbono = 0.0;

    @Column(nullable = false)
    private Double totalDeuda = 0.0;
}

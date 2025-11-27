package com.example.backmapp.repository;

import com.example.backmapp.entity.UsuarioFinanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioFinanzaRepository extends JpaRepository<UsuarioFinanza, String> {
}

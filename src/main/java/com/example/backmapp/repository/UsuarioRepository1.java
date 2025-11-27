package com.example.backmapp.repository;

import com.example.backmapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository1 extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNombreContainingIgnoreCaseOrRutContainingIgnoreCase(String nombre, String rut);

    List<Usuario> findByEstado(Usuario.EstadoUsuario estado);


}

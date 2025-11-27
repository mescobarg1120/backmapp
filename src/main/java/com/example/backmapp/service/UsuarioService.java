package com.example.backmapp.service;

import com.example.backmapp.entity.Usuario;
import com.example.backmapp.repository.UsuarioRepository1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository1 usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorRut(String rut) {
        return usuarioRepository.findById(rut);
    }

    public Usuario crear(Usuario usuario) {
        // Validar que no exista otro usuario con el mismo email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con este email");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(String rut, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(rut);
        if (usuarioExistente.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuario.setRut(rut);
        return usuarioRepository.save(usuario);
    }

    public void eliminar(String rut) {
        if (!usuarioRepository.existsById(rut)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(rut);
    }

    public List<Usuario> buscar(String query) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrRutContainingIgnoreCase(query, query);
    }
}

package com.example.backmapp.service;

import com.example.backmapp.entity.Usuario;
import com.example.backmapp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorRut(String rut) {
        return usuarioRepository.findById(rut);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crear(Usuario usuario) {
        // Validar que no exista otro usuario con el mismo email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con este email");
        }
        // ✅ HASHEAR LA CONTRASEÑA antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(String rut, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorRut(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar solo campos permitidos desde el panel de administración
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());

        // NO actualizar la contraseña desde el panel de administración
        // La contraseña solo se puede cambiar:
        // 1. Al crear el usuario
        // 2. Desde un endpoint específico de cambio de contraseña
        // 3. Por el usuario mismo desde su perfil

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

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElse(null);
    }
}

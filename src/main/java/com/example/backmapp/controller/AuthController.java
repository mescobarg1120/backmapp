package com.example.backmapp.controller;

import com.example.backmapp.entity.LoginRequest;
import com.example.backmapp.entity.LoginResponse;
import com.example.backmapp.entity.Usuario;
import com.example.backmapp.service.JwtService;
import com.example.backmapp.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Buscar usuario en la base de datos
            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail());

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario no encontrado");
            }

            // Generar token
            String token = jwtService.generateToken(usuario.getEmail());

            // Crear respuesta
            LoginResponse response = new LoginResponse(
                    token,
                    usuario.getRut(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getRol().name(),
                    usuario.getEstado().name()
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }
}

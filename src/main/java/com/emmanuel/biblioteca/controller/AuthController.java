package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.security.JwtService;
import com.emmanuel.biblioteca.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Buscar usuario en la base de datos
        Usuario usuario = usuarioService.getUsuarioByUsername(loginRequest.getUsername());

        // Verificar si el usuario existe y si la contraseña es correcta
        if (usuario != null && usuario.getPassword().equals(loginRequest.getPassword())) {
            // Generar token JWT si las credenciales son correctas
            String token = jwtService.generateToken(usuario.getId(), usuario.getRol());
            return ResponseEntity.ok(new JwtResponse(token));
        }

        // Responder con estado 401 si las credenciales son incorrectas
        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }
}

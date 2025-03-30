package com.emmanuelbg10.apirest.controller;

import com.emmanuelbg10.apirest.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validar usuario y contraseña manualmente
        if ("emmanuel".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            // Generar token JWT si las credenciales son correctas
            String token = jwtService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        }
        // Responder con estado 401 si las credenciales son incorrectas
        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }
}
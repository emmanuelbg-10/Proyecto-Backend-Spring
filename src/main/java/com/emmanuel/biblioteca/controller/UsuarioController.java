package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.service.PrestamoService;
import com.emmanuel.biblioteca.service.ResenaService;
import com.emmanuel.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios, sus resenas y sus prestamos")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ResenaService resenaService;
    private final PrestamoService prestamoService;

    public UsuarioController(UsuarioService usuarioService, ResenaService resenaService, PrestamoService prestamoService) {
        this.usuarioService = usuarioService;
        this.resenaService = resenaService;
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getById(@PathVariable("usuarioId") Integer usuarioId) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Usuario> saveUpdate(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveOrUpdate(usuario));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("usuarioId") Integer usuarioId) {
        usuarioService.deleteUsuarioById(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{usuarioId}/resenas")
    public ResponseEntity<List<Resena>> getResenasByUsuario(@PathVariable("usuarioId") Integer usuarioId) {
        return ResponseEntity.ok(resenaService.getResenasByUsuario(usuarioId));
    }

    @GetMapping("/{usuarioId}/libros/{libroId}/resenas/{resenaId}")
    public ResponseEntity<Resena> getResenaUsuario(@PathVariable Integer usuarioId,
                                                   @PathVariable Integer libroId,
                                                   @PathVariable Integer resenaId) {
        return ResponseEntity.ok(resenaService.getResenaByUsuarioYLibro(resenaId, usuarioId, libroId));
    }

    @PostMapping("/{usuarioId}/libros/{libroId}/resenas")
    public ResponseEntity<Resena> saveOrUpdateResena(@Valid @PathVariable Integer usuarioId,
                                                     @PathVariable Integer libroId,
                                                     @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.saveOrUpdateResena(usuarioId, libroId, resena));
    }

    @DeleteMapping("/{usuarioId}/libros/{libroId}/resenas/{resenaId}")
    public ResponseEntity<Void> deleteResena(@PathVariable Integer usuarioId,
                                             @PathVariable Integer libroId,
                                             @PathVariable Integer resenaId) {
        resenaService.deleteResena(usuarioId, libroId, resenaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/prestamos/{copiaLibroId}")
    public ResponseEntity<Prestamo> createPrestamo(@PathVariable Integer usuarioId,
                                                   @PathVariable Integer copiaLibroId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.createPrestamo(usuarioId, copiaLibroId));
    }

    @DeleteMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Integer usuarioId,
                                               @PathVariable Integer prestamoId) {
        prestamoService.deletePrestamo(usuarioId, prestamoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{usuarioId}/prestamos")
    public ResponseEntity<List<Prestamo>> getPrestamosByUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(prestamoService.getPrestamosByUsuario(usuarioId));
    }

    @GetMapping("/{usuarioId}/prestamos/{prestamoId}")
    public ResponseEntity<Prestamo> getPrestamoByUsuario(@PathVariable Integer usuarioId,
                                                         @PathVariable Integer prestamoId) {
        return ResponseEntity.ok(prestamoService.getPrestamoByUsuario(usuarioId, prestamoId));
    }
}

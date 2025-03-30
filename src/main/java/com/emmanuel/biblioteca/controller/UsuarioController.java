package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios, libros, reseñas y préstamos")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final LibroService libroService;
    private final ResenaService resenaService;
    private final PrestamoService prestamoService;
    private final CloudinaryService cloudinaryService;

    public UsuarioController(UsuarioService usuarioService, LibroService libroService, ResenaService resenaService,
                             PrestamoService prestamoService, CloudinaryService cloudinaryService) {
        this.usuarioService = usuarioService;
        this.libroService = libroService;
        this.resenaService = resenaService;
        this.prestamoService = prestamoService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Usuario> saveUpdate(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveOrUpdate(null, usuario));
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuario> saveUpdate(@PathVariable Integer usuarioId, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.saveOrUpdate(usuarioId, usuario));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer usuarioId) {
        usuarioService.deleteUsuarioById(usuarioId);
        return ResponseEntity.noContent().build();
    }

    // Gestión de libros por usuario
    @GetMapping("/{usuarioId}/libros")
    public ResponseEntity<List<Libro>> getLibrosByUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(libroService.getLibrosByUsuario(usuarioId));
    }

    // Obtener un libro específico de un autor (200 OK o excepción manejada)
    @GetMapping("/{usuarioId}/libros/{libroId}")
    public ResponseEntity<Libro> getLibroAutor(
            @PathVariable("usuarioId") Integer usuarioId,
            @PathVariable("libroId") Integer libroId) {
        Libro libro = libroService.getLibroUsuarioById(libroId, usuarioId);
        return ResponseEntity.ok(libro);
    }

    @PostMapping("/{usuarioId}/libros")
    public ResponseEntity<Libro> saveLibro(
            @PathVariable Integer usuarioId,
            @RequestParam("titulo") String titulo,
            @RequestParam("genero") String genero,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Verifica si el usuario tiene rol de AUTOR
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            if (!"AUTOR".equals(usuario.getRol())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Sube la imagen si se proporciona
            String imageUrl = (file != null && !file.isEmpty()) ? cloudinaryService.uploadImage(file) : null;
            // Crea el libro con los datos proporcionados
            Libro libro = new Libro(titulo, genero, usuario, imageUrl);
            // Guarda el libro y devuelve la respuesta
            return ResponseEntity.status(HttpStatus.CREATED).body(libroService.saveOrUpdateLibro(usuarioId, null, libro));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{usuarioId}/libros/{libroId}")
    public ResponseEntity<Libro> updateLibro(
            @PathVariable Integer usuarioId,
            @PathVariable Integer libroId,
            @RequestParam("titulo") String titulo,
            @RequestParam("genero") String genero,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Verifica si el usuario tiene rol de AUTOR
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            if (!"AUTOR".equals(usuario.getRol())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Si hay un archivo, se sube la nueva imagen; si no, se mantiene la imagen anterior
            String imageUrl = (file != null && !file.isEmpty()) ? cloudinaryService.uploadImage(file) : null;

            // Crea el libro con los datos proporcionados
            Libro libro = new Libro(titulo, genero, usuario, imageUrl);
            // Actualiza el libro con el ID proporcionado
            return ResponseEntity.ok(libroService.saveOrUpdateLibro(usuarioId, libroId, libro));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @DeleteMapping("/{usuarioId}/libros/{libroId}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Integer usuarioId, @PathVariable Integer libroId) {
        libroService.deleteLibro(libroId, usuarioId);
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
    public ResponseEntity<Resena> saveResena(
            @PathVariable Integer usuarioId,
            @PathVariable Integer libroId,
            @Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resenaService.saveOrUpdateResena(usuarioId, libroId, null, resena));
    }

    @PutMapping("/{usuarioId}/libros/{libroId}/resenas/{resenaId}")
    public ResponseEntity<Resena> updateResena(
            @PathVariable Integer usuarioId,
            @PathVariable Integer libroId,
            @PathVariable Integer resenaId,
            @Valid @RequestBody Resena resena) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resenaService.saveOrUpdateResena(usuarioId, libroId, resenaId, resena));
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

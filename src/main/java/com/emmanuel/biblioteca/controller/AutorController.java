package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Autor;
import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.service.AutorService;
import com.emmanuel.biblioteca.service.LibroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/autores")
@Tag(name = "Autores", description = "Gestión de autores y sus libros")
public class AutorController {

    private final AutorService autorService;
    private final LibroService libroService;

    public AutorController(AutorService autorService, LibroService libroService) {
        this.autorService = autorService;
        this.libroService = libroService;
    }

    // Obtener todos los autores (200 OK)
    @GetMapping
    public ResponseEntity<List<Autor>> getAll() {
        List<Autor> autores = autorService.getAutores();
        return ResponseEntity.ok(autores);
    }

    // Obtener un autor por ID (200 OK o excepción manejada)
    @GetMapping("/{autorId}")
    public ResponseEntity<Autor> getById(@PathVariable("autorId") Integer autorId) {
        Autor autor = autorService.getAutorById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor con ID " + autorId + " no encontrado"));
        return ResponseEntity.ok(autor);
    }

    // Crear o actualizar un autor (201 Created)
    @PostMapping
    public ResponseEntity<Autor> saveUpdate(@Valid @RequestBody Autor autor) {
        Autor savedAutor = autorService.saveOrUpdate(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAutor);
    }

    // Eliminar un autor (204 No Content o excepción manejada)
    @DeleteMapping("/{autorId}")
    public ResponseEntity<Void> deleteAutor(@PathVariable("autorId") Integer autorId) {
        autorService.deleteAutorById(autorId);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los libros de un autor (200 OK)
    @GetMapping("/{autorId}/libros")
    public ResponseEntity<List<Libro>> getLibrosByAutor(@PathVariable("autorId") Integer autorId) {
        List<Libro> libros = libroService.getLibrosByAutor(autorId);
        return ResponseEntity.ok(libros);
    }

    // Obtener un libro específico de un autor (200 OK o excepción manejada)
    @GetMapping("/{autorId}/libros/{libroId}")
    public ResponseEntity<Libro> getLibroAutor(
            @PathVariable("autorId") Integer autorId,
            @PathVariable("libroId") Integer libroId) {
        Libro libro = libroService.getLibroAutorById(libroId, autorId);
        return ResponseEntity.ok(libro);
    }

    @PostMapping("/{autorId}/libros")
    public ResponseEntity<Libro> saveOrUpdateLibro(
            @PathVariable Integer autorId,
            @Valid @RequestBody Libro libro) {
        Libro savedLibro = libroService.saveOrUpdateLibro(autorId, libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLibro);
    }

    @DeleteMapping("/{autorId}/libros/{libroId}")
    public ResponseEntity<Void> deleteLibro(
            @PathVariable Integer autorId,
            @PathVariable Integer libroId) {
        libroService.deleteLibro(libroId, autorId);
        return ResponseEntity.noContent().build();
    }
}

package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.service.LibroService;
import com.emmanuel.biblioteca.service.ResenaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/libros")
@Tag(name = "Libro", description = "Gestión de libro")
public class LibroController {
    private final LibroService libroService;
    private final ResenaService resenaService;

    public LibroController(LibroService libroService, ResenaService resenaService) {
        this.libroService = libroService;
        this.resenaService = resenaService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> getAll() {
        return ResponseEntity.ok(libroService.getLibros());
    }

    @GetMapping("/{libroId}")
    public ResponseEntity<Libro> getById(@PathVariable("libroId") Integer libroId) {
        return ResponseEntity.ok(libroService.getLibroById(libroId));
    }

    @GetMapping("/{libroId}/resenas")
    public ResponseEntity<List<Resena>> getResenasByLibro(@PathVariable("libroId") Integer libroId) {
        return ResponseEntity.ok(resenaService.getResenasByLibro(libroId));
    }

    @GetMapping("/{libroId}/resenas/{resenaId}")
    public ResponseEntity<Resena> getByIdLibroResena(
            @PathVariable("libroId") Integer libroId,
            @PathVariable("resenaId") Integer resenaId) {
        return ResponseEntity.ok(resenaService.getResenaLibroById(resenaId, libroId));
    }
}
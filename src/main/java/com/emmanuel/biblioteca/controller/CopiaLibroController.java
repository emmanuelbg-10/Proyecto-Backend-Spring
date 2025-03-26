package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.service.CopiaLibroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/copiaLibros")
@Tag(name = "CopiaLibros", description = "Gestión de copias de libros")
public class CopiaLibroController {

    private final CopiaLibroService copiaLibroService;

    public CopiaLibroController(CopiaLibroService copiaLibroService) {
        this.copiaLibroService = copiaLibroService;
    }

    @GetMapping
    public ResponseEntity<List<CopiaLibro>> getAll() {
        return ResponseEntity.ok(copiaLibroService.getCopiaLibros());
    }

    @GetMapping("/{libroId}")
    public ResponseEntity<CopiaLibro> getById(@PathVariable("libroId") Integer libroId) {
        return ResponseEntity.ok(copiaLibroService.getCopiaLibroById(libroId));
    }

    @PostMapping("/{libroId}")
    public ResponseEntity<CopiaLibro> saveOrUpdate(@PathVariable Integer libroId) {
        CopiaLibro savedCopia = copiaLibroService.saveOrUpdateCopiaLibro(libroId);
        return ResponseEntity.ok(savedCopia);
    }


    @DeleteMapping("/{copiaLibroId}")
    public ResponseEntity<Void> deleteLibro(@PathVariable("copiaLibroId") Integer copiaLibroId) {
        copiaLibroService.deleteCopiaLibroById(copiaLibroId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<CopiaLibro>> getLibrosDisponibles() {
        List<CopiaLibro> disponibles = copiaLibroService.getLibrosDisponibles();
        return ResponseEntity.ok(disponibles);
    }
}
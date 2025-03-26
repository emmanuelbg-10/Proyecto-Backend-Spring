package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.service.ResenaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/resenas")
@Tag(name = "Resena", description = "Gestión de resena")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public ResponseEntity<List<Resena>> getAll() {
        return ResponseEntity.ok(resenaService.getResenas());
    }

    @GetMapping("/{resenaId}")
    public ResponseEntity<Resena> getById(@PathVariable("resenaId") Integer resenaId) {
        return ResponseEntity.ok(resenaService.getResenaById(resenaId));
    }
}
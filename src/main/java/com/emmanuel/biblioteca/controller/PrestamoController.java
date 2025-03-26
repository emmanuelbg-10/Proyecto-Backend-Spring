package com.emmanuel.biblioteca.controller;

import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/prestamos")
@Tag(name = "Prestamos", description = "Gestión de prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> getAll() {
        return ResponseEntity.ok(prestamoService.getPrestamos());
    }

    @GetMapping("/{prestamoId}")
    public ResponseEntity<Prestamo> getById(@PathVariable("prestamoId") Integer prestamoId) {
        return ResponseEntity.ok(prestamoService.getPrestamoById(prestamoId));
    }

    @GetMapping("/top-usuarios")
    public ResponseEntity<List<Object[]>> getUsuariosConMasPrestamosUltimoAnio(
            @RequestParam(required = false) Integer maxResultados) {

        if (maxResultados == null) {
            maxResultados = prestamoService.getTotalUsuariosConPrestamosUltimoAnio(); // Calcula automáticamente
        }

        List<Object[]> resultado = prestamoService.getUsuariosConMasPrestamosUltimoAnio(maxResultados);
        return ResponseEntity.ok(resultado);
    }

}
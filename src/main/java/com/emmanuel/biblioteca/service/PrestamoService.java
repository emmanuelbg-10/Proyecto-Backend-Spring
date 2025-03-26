package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.PrestamoRepository;
import com.emmanuel.biblioteca.repository.CopiaLibroRepository;
import com.emmanuel.biblioteca.repository.PrestamoRepositoryCustom;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final CopiaLibroRepository copiaLibroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepositoryCustom prestamoRepositoryCustom;

    public PrestamoService(PrestamoRepository prestamoRepository, CopiaLibroRepository copiaLibroRepository, UsuarioRepository usuarioRepository, PrestamoRepositoryCustom prestamoRepositoryCustom) {
        this.prestamoRepository = prestamoRepository;
        this.copiaLibroRepository = copiaLibroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepositoryCustom = prestamoRepositoryCustom;
    }

    public List<Prestamo> getPrestamos() {
        return prestamoRepository.findAll();
    }

    public Prestamo getPrestamoById(Integer id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el préstamo con ID " + id));
    }

    public void deletePrestamo(Integer usuarioId, Integer prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("El préstamo no pertenece a este usuario");
        }

        prestamoRepository.delete(prestamo);
    }

    public List<Prestamo> getPrestamosByUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return prestamoRepository.findByUsuario(usuario);
    }

    public Prestamo getPrestamoByUsuario(Integer usuarioId, Integer prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (!prestamo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("El préstamo no pertenece a este usuario");
        }

        return prestamo;
    }

    @Transactional
    public List<Object[]> getUsuariosConMasPrestamosUltimoAnio(int maxResultados) {
        if (maxResultados <= 0) {
            throw new IllegalArgumentException("El número de resultados debe ser mayor a 0");
        }

        try {
            List<Object[]> resultado = prestamoRepositoryCustom.getUsuariosConMasPrestamosUltimoAnio(maxResultados);

            if (resultado.isEmpty()) {
                throw new RuntimeException("No se encontraron usuarios con préstamos en el último año");
            }

            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los usuarios con más préstamos: " + e.getMessage(), e);
        }
    }

    public Prestamo createPrestamo(Integer usuarioId, Integer copiaLibroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        CopiaLibro copiaLibro = copiaLibroRepository.findById(copiaLibroId)
                .orElseThrow(() -> new RuntimeException("Copia del libro no encontrada"));

        // Verificar si la copia ya está prestada
        boolean copiaPrestada = prestamoRepository.existsByCopiaLibroAndDevueltoFalse(copiaLibro);
        if (copiaPrestada) {
            throw new RuntimeException("Esta copia del libro ya está prestada");
        }

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaVencimiento = fechaInicio.plusDays(14);

        // Crear el préstamo con fechaDevolucion en null y devuelto en false
        Prestamo prestamo = new Prestamo(usuario, copiaLibro, fechaInicio, fechaVencimiento, null, false);

        // Marcar la copia del libro como no disponible
        copiaLibro.setDisponible(false);
        copiaLibroRepository.save(copiaLibro);

        return prestamoRepository.save(prestamo);
    }

    public int getTotalUsuariosConPrestamosUltimoAnio() {
        return prestamoRepositoryCustom.countUsuariosConPrestamosUltimoAnio();
    }
}

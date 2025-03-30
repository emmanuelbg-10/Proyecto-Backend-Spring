package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.exception.InvalidRequestException;
import com.emmanuel.biblioteca.exception.UnauthorizedAccessException;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.exception.ResourceNotFoundException;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public LibroService(LibroRepository libroRepository, UsuarioRepository usuarioRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Libro> getLibros() {
        return libroRepository.findAll();
    }

    public Libro getLibroById(Integer id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con ID " + id + " no encontrado"));
    }

    public List<Libro> getLibrosByUsuario(Integer usuarioId) {
        List<Libro> libros = libroRepository.findByUsuarioId(usuarioId);
        if (libros.isEmpty()) {
            throw new ResourceNotFoundException("No hay libros para el usuario con ID " + usuarioId);
        }
        return libros;
    }

    public Libro getLibroUsuarioById(Integer libroId, Integer usuarioId) {
        return libroRepository.findByIdAndUsuarioId(libroId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No hay un libro con ID " + libroId + " para este usuario"));
    }

    public Libro saveOrUpdateLibro(Integer usuarioId, Integer libroId, Libro libro) {
        // Verificar si el usuario existe
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario con ID " + usuarioId + " no encontrado");
        }

        if (libroId != null) {
            // 🟢 Actualizar un libro existente
            Libro libroExistente = libroRepository.findById(libroId)
                    .orElseThrow(() -> new ResourceNotFoundException("El libro con ID " + libroId + " no existe"));

            if (!libroExistente.getUsuario().getId().equals(usuarioId)) {
                throw new UnauthorizedAccessException("El libro con ID " + libroId + " no pertenece al usuario con ID " + usuarioId);
            }

            // 📌 Actualizar solo los campos permitidos
            libroExistente.setTitulo(libro.getTitulo());
            libroExistente.setGenero(libro.getGenero());

            // 📌 Si el usuario subió una nueva imagen, la actualizamos
            if (libro.getImagenUrl() != null && !libro.getImagenUrl().isBlank()) {
                libroExistente.setImagenUrl(libro.getImagenUrl());
            }

            return libroRepository.save(libroExistente);
        } else {
            // 🟢 Crear un libro nuevo
            if (libro.getImagenUrl() == null || libro.getImagenUrl().isBlank()) {
                throw new InvalidRequestException("El libro debe tener una imagen.");
            }

            libro.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow(
                    () -> new ResourceNotFoundException("Usuario con ID " + usuarioId + " no encontrado")));
            return libroRepository.save(libro);
        }
    }

    public void deleteLibro(Integer libroId, Integer usuarioId) {
        // Verificar si el usuario existe
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario con ID " + usuarioId + " no encontrado");
        }

        // Buscar el libro y verificar si pertenece al usuario
        Libro libro = libroRepository.findByIdAndUsuarioId(libroId, usuarioId)
                .orElseThrow(() -> {
                    // Si el libro no existe, lanzamos un ResourceNotFoundException
                    if (!libroRepository.existsById(libroId)) {
                        return new ResourceNotFoundException("El libro con ID " + libroId + " no encontrado");
                    }
                    // Si el libro no pertenece al usuario, lanzamos UnauthorizedAccessException
                    return new UnauthorizedAccessException("El libro con ID " + libroId + " no pertenece al usuario con ID " + usuarioId);
                });
        libroRepository.delete(libro);
    }
}


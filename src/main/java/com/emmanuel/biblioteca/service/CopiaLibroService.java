package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.exception.InvalidRequestException;
import com.emmanuel.biblioteca.repository.CopiaLibroRepository;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CopiaLibroService {
    private final CopiaLibroRepository copiaLibroRepository;
    private final LibroRepository libroRepository;

    public CopiaLibroService(CopiaLibroRepository copiaLibroRepository, LibroRepository libroRepository) {
        this.copiaLibroRepository = copiaLibroRepository;
        this.libroRepository = libroRepository;
    }

    public List<CopiaLibro> getCopiaLibros() {
        return copiaLibroRepository.findAll();
    }

    public CopiaLibro getCopiaLibroById(Integer id) {
        return copiaLibroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Copia de libro no encontrada con ID: " + id));
    }

    public void deleteCopiaLibroById(Integer id) {
        CopiaLibro copiaLibro = getCopiaLibroById(id); // Lanza excepción si no existe

        if (!copiaLibro.isDisponible()) {
            throw new InvalidRequestException("No se puede eliminar una copia de libro que está actualmente prestada.");
        }

        copiaLibroRepository.delete(copiaLibro);
    }


    public CopiaLibro saveOrUpdateCopiaLibro(Integer libroId) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + libroId));

        CopiaLibro nuevaCopia = new CopiaLibro(libro); // Siempre se crea una nueva copia
        return copiaLibroRepository.save(nuevaCopia);
    }

    public List<CopiaLibro> getLibrosDisponibles() {
        try {
            List<CopiaLibro> disponibles = copiaLibroRepository.findLibrosDisponibles();

            if (disponibles.isEmpty()) {
                throw new ResourceNotFoundException("No hay libros disponibles en este momento");
            }

            return disponibles;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al obtener libros disponibles: " + e.getMessage());
        }
    }

    public List<CopiaLibro> getCopiasDisponiblesByLibroId(Integer libroId) {
        // Verificar si el libro existe
        if (!libroRepository.existsById(libroId)) {
            throw new ResourceNotFoundException("Libro con ID " + libroId + " no encontrado");
        }

        List<CopiaLibro> copiasDisponibles = copiaLibroRepository.findByLibroIdAndDisponible(libroId, true);

        if (copiasDisponibles.isEmpty()) {
            throw new ResourceNotFoundException("No hay copias disponibles para el libro con ID " + libroId);
        }

        return copiasDisponibles;
    }
}

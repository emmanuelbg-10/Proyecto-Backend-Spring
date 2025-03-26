package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Autor;
import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.repository.AutorRepository;
import com.emmanuel.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public List<Libro> getLibros() {
        return libroRepository.findAll();
    }

    public Libro getLibroById(Integer id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro con ID " + id + " no encontrado"));
    }

    public List<Libro> getLibrosByAutor(Integer autorId) {
        List<Libro> libros = libroRepository.findByAutorId(autorId);
        if (libros.isEmpty()) {
            throw new RuntimeException("No hay libros para el autor con ID " + autorId);
        }
        return libros;
    }

    public Libro getLibroAutorById(Integer libroId, Integer autorId) {
        return libroRepository.findByIdAndAutorId(libroId, autorId)
                .orElseThrow(() -> new RuntimeException("No hay un libro con ID " + libroId + " para este autor"));
    }

    public Libro saveOrUpdateLibro(Integer autorId, Libro libro) {
        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor con ID " + autorId + " no encontrado"));

        libro.setAutor(autor);
        return libroRepository.save(libro);
    }

    public void deleteLibro(Integer libroId, Integer autorId) {
        if (!autorRepository.existsById(autorId)) {
            throw new RuntimeException("Autor con ID " + autorId + " no encontrado");
        }

        Libro libro = libroRepository.findByIdAndAutorId(libroId, autorId)
                .orElseThrow(() -> new RuntimeException(
                        "El libro con ID " + libroId + " no existe o no pertenece al autor con ID " + autorId));

        libroRepository.delete(libro);
    }
}

package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.exception.UnauthorizedAccessException;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.repository.ResenaRepository;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import com.emmanuel.biblioteca.exception.ResourceNotFoundException;
import com.emmanuel.biblioteca.exception.InvalidRequestException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {
    private final ResenaRepository resenaRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public ResenaService(ResenaRepository resenaRepository, LibroRepository libroRepository, UsuarioRepository usuarioRepository) {
        this.resenaRepository = resenaRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Resena> getResenas() {
        return resenaRepository.findAll();
    }

    public List<Resena> getResenasByUsuario(Integer usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public List<Resena> getResenasByLibro(Integer libroId) {
        return resenaRepository.findByLibroId(libroId);
    }

    public Resena getResenaByUsuarioYLibro(Integer resenaId, Integer usuarioId, Integer libroId) {
        return resenaRepository.findByIdAndUsuarioIdAndLibroId(resenaId, usuarioId, libroId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reseña con ID " + resenaId +
                        " del usuario con ID " + usuarioId + " para el libro con ID " + libroId));
    }

    public Resena saveOrUpdateResena(Integer usuarioId, Integer libroId, Integer resenaId, Resena resena) {
        if (resena == null || resena.getComentario() == null || resena.getComentario().isEmpty()) {
            throw new InvalidRequestException("La reseña debe tener un comentario válido.");
        }

        // Verificar que la calificación está en el rango permitido
        if (resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            throw new InvalidRequestException("La calificación debe estar entre 1 y 5.");
        }

        // Buscar usuario y libro
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con ID " + libroId + " no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + usuarioId + " no encontrado"));

        if (resenaId != null) {
            // Buscar la reseña existente
            Resena resenaExistente = resenaRepository.findById(resenaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reseña con ID " + resenaId + " no encontrada"));

            // Validar que la reseña pertenece al usuario Y al libro
            if (!resenaExistente.getUsuario().getId().equals(usuarioId) || !resenaExistente.getLibro().getId().equals(libroId)) {
                throw new UnauthorizedAccessException("No puedes modificar una reseña que no te pertenece o no es del libro indicado.");
            }

            // Actualizar la reseña existente
            resenaExistente.setComentario(resena.getComentario());
            resenaExistente.setCalificacion(resena.getCalificacion());
            return resenaRepository.save(resenaExistente);
        } else {

            // Guardar nueva reseña
            resena.setLibro(libro);
            resena.setUsuario(usuario);
            return resenaRepository.save(resena);
        }
    }



    public void deleteResena(Integer usuarioId, Integer libroId, Integer resenaId) {
        Resena resena = resenaRepository.findByIdAndUsuarioIdAndLibroId(resenaId, usuarioId, libroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La reseña con ID " + resenaId + " no existe o no pertenece al usuario " + usuarioId));

        resenaRepository.delete(resena);
    }

    public Resena getResenaById(Integer resenaId) {
        return resenaRepository.findById(resenaId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reseña con ID " + resenaId));
    }

    public Resena getResenaLibroById(Integer resenaId, Integer libroId) {
        return resenaRepository.findByIdAndLibroId(resenaId, libroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la reseña con ID " + resenaId + " para el libro con ID " + libroId));
    }
}

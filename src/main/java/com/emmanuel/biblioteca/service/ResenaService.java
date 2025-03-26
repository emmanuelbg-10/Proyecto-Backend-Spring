package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.repository.PrestamoRepositoryCustom;
import com.emmanuel.biblioteca.repository.ResenaRepository;
import com.emmanuel.biblioteca.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {
    private final ResenaRepository resenaRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepositoryCustom prestamoRepositoryCustom;

    public ResenaService(ResenaRepository resenaRepository, LibroRepository libroRepository, UsuarioRepository usuarioRepository, PrestamoRepositoryCustom prestamoRepositoryCustom) {
        this.resenaRepository = resenaRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepositoryCustom = prestamoRepositoryCustom;
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
                .orElseThrow(() -> new RuntimeException("No se encontró la reseña con ID " + resenaId
                        + " del usuario " + usuarioId + " para el libro " + libroId));
    }

    public Resena saveOrUpdateResena(Integer usuarioId, Integer libroId, Resena resena) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro con ID " + libroId + " no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + usuarioId + " no encontrado"));

        resena.setLibro(libro);
        resena.setUsuario(usuario);
        return resenaRepository.save(resena);
    }

    public void deleteResena(Integer usuarioId, Integer libroId, Integer resenaId) {
        Resena resena = resenaRepository.findByIdAndUsuarioIdAndLibroId(resenaId, usuarioId, libroId)
                .orElseThrow(() -> new RuntimeException(
                        "La reseña con ID " + resenaId + " no existe o no pertenece al usuario " + usuarioId));

        resenaRepository.delete(resena);
    }

    public Resena getResenaById(Integer resenaId) {
        return resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("No se encontró la reseña con ID " + resenaId));
    }

    public Resena getResenaLibroById(Integer resenaId, Integer libroId) {
        return resenaRepository.findByIdAndLibroId(resenaId, libroId)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró la reseña con ID " + resenaId + " para el libro con ID " + libroId));
    }
}

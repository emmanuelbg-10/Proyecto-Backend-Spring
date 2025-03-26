package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    // Obtener todas las reseñas de un usuario
    List<Resena> findByUsuarioId(Integer usuarioId);

    // Obtener todas las reseñas de un libro
    List<Resena> findByLibroId(Integer libroId);

    // Obtener una reseña específica de un usuario sobre un libro
    Optional<Resena> findByIdAndUsuarioIdAndLibroId(Integer resenaId, Integer usuarioId, Integer libroId);

    Optional<Resena> findByIdAndLibroId(Integer resenaId, Integer libroId);

}

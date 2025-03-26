package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {
    List<Libro> findByAutorId(Integer autorId);
    Optional<Libro> findByIdAndAutorId(Integer libroId, Integer autorId);
}

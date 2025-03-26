package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopiaLibroRepository extends JpaRepository<CopiaLibro, Integer> {
    @Query("SELECT c FROM CopiaLibro c WHERE c.disponible = true")
    List<CopiaLibro> findLibrosDisponibles();

}

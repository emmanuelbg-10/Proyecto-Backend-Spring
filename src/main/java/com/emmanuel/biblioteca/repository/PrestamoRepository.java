package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    List<Prestamo> findByUsuario(Usuario usuario);
    boolean existsByCopiaLibroAndDevueltoFalse(CopiaLibro copiaLibro);
}

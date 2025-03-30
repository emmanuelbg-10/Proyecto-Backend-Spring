package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(Long telefono);
}

package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getUsuarios() {
        try {
            return usuarioRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener la lista de usuarios", e);
        }
    }

    @Transactional
    public Usuario getUsuarioById(Integer usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
            Hibernate.initialize(usuario.getResenas());  // Cargar lista de reseñas
            Hibernate.initialize(usuario.getPrestamos()); // Cargar lista de préstamos
            return usuario;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener el usuario con ID: " + usuarioId, e);
        }
    }

    public Usuario saveOrUpdate(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al guardar o actualizar el usuario", e);
        }
    }

    public void deleteUsuarioById(Integer usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
            usuarioRepository.delete(usuario);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al eliminar el usuario con ID: " + usuarioId, e);
        }
    }
}

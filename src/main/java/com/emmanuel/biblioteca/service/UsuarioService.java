package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import com.emmanuel.biblioteca.exception.ResourceNotFoundException;
import com.emmanuel.biblioteca.exception.InvalidRequestException;
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
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Integer usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

            Hibernate.initialize(usuario.getResenas());  // Cargar lista de reseñas
            Hibernate.initialize(usuario.getPrestamos()); // Cargar lista de préstamos
            return usuario;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener el usuario con ID: " + usuarioId, e);
        }
    }

    public Usuario getUsuarioByUsername(String username) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(username);
            Hibernate.initialize(usuario.getResenas());
            Hibernate.initialize(usuario.getPrestamos());
            return usuario;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al obtener el usuario con username: " + username, e);
        }
    }


    public Usuario saveOrUpdate(Integer usuarioId, Usuario usuario) {
        if (usuario == null || usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            throw new InvalidRequestException("El usuario debe tener un nombre válido.");
        }

        if (usuarioId != null) {
            // Buscar usuario existente
            Usuario usuarioExistente = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + usuarioId + " no encontrado"));

            // Actualizar solo los campos permitidos
            usuarioExistente.setUsername(usuario.getUsername());
            usuarioExistente.setCorreo(usuario.getCorreo());
            usuarioExistente.setTelefono(usuario.getTelefono());

            try {
                return usuarioRepository.save(usuarioExistente);
            } catch (DataAccessException e) {
                throw new RuntimeException("Error al actualizar el usuario", e);
            }
        } else {
            // Crear nuevo usuario
            try {
                return usuarioRepository.save(usuario);
            } catch (DataAccessException e) {
                throw new RuntimeException("Error al guardar el usuario", e);
            }
        }
    }


    public void deleteUsuarioById(Integer usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));
            usuarioRepository.delete(usuario);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al eliminar el usuario con ID: " + usuarioId, e);
        }
    }
}

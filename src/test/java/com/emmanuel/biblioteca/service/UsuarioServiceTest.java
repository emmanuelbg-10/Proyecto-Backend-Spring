package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNombre("Juan Perez");

        usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNombre("Maria Gomez");
    }

    @Test
    void testGetUsuarios() {
        List<Usuario> usuariosMock = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuariosMock);

        List<Usuario> resultado = usuarioService.getUsuarios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testGetUsuarios_DataAccessException() {
        when(usuarioRepository.findAll()).thenThrow(new DataAccessException("Error de BD") {});

        Exception exception = assertThrows(RuntimeException.class, () -> usuarioService.getUsuarios());

        assertEquals("Error al obtener la lista de usuarios", exception.getMessage());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testGetUsuarioById_Encontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        Usuario resultado = usuarioService.getUsuarioById(1);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());

        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void testGetUsuarioById_NoEncontrado() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> usuarioService.getUsuarioById(99));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99);
    }

    @Test
    void testSaveOrUpdate() {
        when(usuarioRepository.save(usuario1)).thenReturn(usuario1);

        Usuario resultado = usuarioService.saveOrUpdate(usuario1);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());

        verify(usuarioRepository, times(1)).save(usuario1);
    }

    @Test
    void testSaveOrUpdate_DataAccessException() {
        when(usuarioRepository.save(usuario1)).thenThrow(new DataAccessException("Error de BD") {});

        Exception exception = assertThrows(RuntimeException.class, () -> usuarioService.saveOrUpdate(usuario1));

        assertEquals("Error al guardar o actualizar el usuario", exception.getMessage());
        verify(usuarioRepository, times(1)).save(usuario1);
    }

    @Test
    void testDeleteUsuarioById_Existente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario1));
        doNothing().when(usuarioRepository).delete(usuario1);

        assertDoesNotThrow(() -> usuarioService.deleteUsuarioById(1));

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).delete(usuario1);
    }

    @Test
    void testDeleteUsuarioById_NoExistente() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> usuarioService.deleteUsuarioById(99));

        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99);
        verify(usuarioRepository, times(0)).delete(any(Usuario.class));
    }
}

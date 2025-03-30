package com.emmanuel.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Resena;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.repository.PrestamoRepositoryCustom;
import com.emmanuel.biblioteca.repository.ResenaRepository;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepositoryCustom prestamoRepositoryCustom;

    @InjectMocks
    private ResenaService resenaService;

    private Usuario usuario;
    private Libro libro;
    private Resena resena;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);

        libro = new Libro();
        libro.setId(1);

        resena = new Resena();
        resena.setId(1);
        resena.setUsuario(usuario);
        resena.setLibro(libro);
    }

    @Test
    void testGetResenas() {
        when(resenaRepository.findAll()).thenReturn(List.of(resena));
        List<Resena> resultado = resenaService.getResenas();
        assertFalse(resultado.isEmpty());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    void testGetResenasByUsuario() {
        when(resenaRepository.findByUsuarioId(1)).thenReturn(List.of(resena));
        List<Resena> resultado = resenaService.getResenasByUsuario(1);
        assertFalse(resultado.isEmpty());
        verify(resenaRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    void testGetResenasByLibro() {
        when(resenaRepository.findByLibroId(1)).thenReturn(List.of(resena));
        List<Resena> resultado = resenaService.getResenasByLibro(1);
        assertFalse(resultado.isEmpty());
        verify(resenaRepository, times(1)).findByLibroId(1);
    }

    @Test
    void testGetResenaByUsuarioYLibro_NotFound() {
        when(resenaRepository.findByIdAndUsuarioIdAndLibroId(1, 1, 1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> resenaService.getResenaByUsuarioYLibro(1, 1, 1));
        assertEquals("No se encontró la reseña con ID 1 del usuario 1 para el libro 1", exception.getMessage());
    }

    @Test
    void testSaveOrUpdateResena() {
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        Resena resultado = resenaService.saveOrUpdateResena(1, 1, 1, resena);        assertNotNull(resultado);
        verify(resenaRepository, times(1)).save(resena);
    }

    @Test
    void testDeleteResena_NotFound() {
        when(resenaRepository.findByIdAndUsuarioIdAndLibroId(1, 1, 1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> resenaService.deleteResena(1, 1, 1));
        assertEquals("La reseña con ID 1 no existe o no pertenece al usuario 1", exception.getMessage());
    }

    @Test
    void testGetResenaById_NotFound() {
        when(resenaRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> resenaService.getResenaById(1));
        assertEquals("No se encontró la reseña con ID 1", exception.getMessage());
    }
}
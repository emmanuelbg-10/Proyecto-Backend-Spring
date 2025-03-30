package com.emmanuel.biblioteca.service;


import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.LibroRepository;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro1;
    private Libro libro2;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("Gabriel Garcia Marquez");

        libro1 = new Libro();
        libro1.setId(1);
        libro1.setTitulo("Cien años de soledad");
        libro1.setUsuario(usuario);

        libro2 = new Libro();
        libro2.setId(2);
        libro2.setTitulo("El otoño del patriarca");
        libro2.setUsuario(usuario);
    }

    @Test
    void testGetLibros() {
        List<Libro> librosMock = Arrays.asList(libro1, libro2);
        when(libroRepository.findAll()).thenReturn(librosMock);

        List<Libro> resultado = libroService.getLibros();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTitulo());

        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void testGetLibroById_Encontrado() {
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro1));

        Libro resultado = libroService.getLibroById(1);

        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());

        verify(libroRepository, times(1)).findById(1);
    }

    @Test
    void testGetLibroById_NoEncontrado() {
        when(libroRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> libroService.getLibroById(99));

        assertEquals("Libro con ID 99 no encontrado", exception.getMessage());
        verify(libroRepository, times(1)).findById(99);
    }

    @Test
    void testGetLibrosByUsuario() {
        when(libroRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(libro1, libro2));

        List<Libro> resultado = libroService.getLibrosByUsuario(1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(libroRepository, times(1)).findByUsuarioId(1);
    }

    @Test
    void testGetLibrosByUsuario_NoEncontrado() {
        when(libroRepository.findByUsuarioId(99)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> libroService.getLibrosByUsuario(99));

        assertEquals("No hay libros para el autor con ID 99", exception.getMessage());
        verify(libroRepository, times(1)).findByUsuarioId(99);
    }

    @Test
    void testSaveOrUpdateLibro() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(libroRepository.save(libro1)).thenReturn(libro1);

        Libro resultado = libroService.saveOrUpdateLibro(1, 1, libro1);
        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());
        assertEquals(usuario, resultado.getUsuario());

        verify(usuarioRepository, times(1)).findById(1);
        verify(libroRepository, times(1)).save(libro1);
    }

    @Test
    void testDeleteLibro() {
        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(libroRepository.findByIdAndUsuarioId(1, 1)).thenReturn(Optional.of(libro1));
        doNothing().when(libroRepository).delete(libro1);

        assertDoesNotThrow(() -> libroService.deleteLibro(1, 1));

        verify(usuarioRepository, times(1)).existsById(1);
        verify(libroRepository, times(1)).findByIdAndUsuarioId(1, 1);
        verify(libroRepository, times(1)).delete(libro1);
    }
}
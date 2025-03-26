package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Autor;
import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.repository.AutorRepository;
import com.emmanuel.biblioteca.repository.LibroRepository;
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
    private AutorRepository autorRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro1;
    private Libro libro2;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1);
        autor.setNombre("Gabriel Garcia Marquez");

        libro1 = new Libro();
        libro1.setId(1);
        libro1.setTitulo("Cien años de soledad");
        libro1.setAutor(autor);

        libro2 = new Libro();
        libro2.setId(2);
        libro2.setTitulo("El otoño del patriarca");
        libro2.setAutor(autor);
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
    void testGetLibrosByAutor() {
        when(libroRepository.findByAutorId(1)).thenReturn(Arrays.asList(libro1, libro2));

        List<Libro> resultado = libroService.getLibrosByAutor(1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(libroRepository, times(1)).findByAutorId(1);
    }

    @Test
    void testGetLibrosByAutor_NoEncontrado() {
        when(libroRepository.findByAutorId(99)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> libroService.getLibrosByAutor(99));

        assertEquals("No hay libros para el autor con ID 99", exception.getMessage());
        verify(libroRepository, times(1)).findByAutorId(99);
    }

    @Test
    void testSaveOrUpdateLibro() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
        when(libroRepository.save(libro1)).thenReturn(libro1);

        Libro resultado = libroService.saveOrUpdateLibro(1, libro1);

        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getTitulo());
        assertEquals(autor, resultado.getAutor());

        verify(autorRepository, times(1)).findById(1);
        verify(libroRepository, times(1)).save(libro1);
    }

    @Test
    void testDeleteLibro() {
        when(autorRepository.existsById(1)).thenReturn(true);
        when(libroRepository.findByIdAndAutorId(1, 1)).thenReturn(Optional.of(libro1));
        doNothing().when(libroRepository).delete(libro1);

        assertDoesNotThrow(() -> libroService.deleteLibro(1, 1));

        verify(autorRepository, times(1)).existsById(1);
        verify(libroRepository, times(1)).findByIdAndAutorId(1, 1);
        verify(libroRepository, times(1)).delete(libro1);
    }
}
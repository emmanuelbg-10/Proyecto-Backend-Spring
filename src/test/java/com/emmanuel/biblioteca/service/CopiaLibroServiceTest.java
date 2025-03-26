package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.entity.Libro;
import com.emmanuel.biblioteca.repository.CopiaLibroRepository;
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
class CopiaLibroServiceTest {

    @Mock
    private CopiaLibroRepository copiaLibroRepository;

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private CopiaLibroService copiaLibroService;

    private CopiaLibro copia1;
    private CopiaLibro copia2;
    private Libro libro;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("El Quijote");

        copia1 = new CopiaLibro(libro);
        copia1.setId(1);

        copia2 = new CopiaLibro(libro);
        copia2.setId(2);
    }

    @Test
    void testGetCopiaLibros() {
        List<CopiaLibro> copiasMock = Arrays.asList(copia1, copia2);
        when(copiaLibroRepository.findAll()).thenReturn(copiasMock);

        List<CopiaLibro> resultado = copiaLibroService.getCopiaLibros();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(copiaLibroRepository, times(1)).findAll();
    }

    @Test
    void testGetCopiaLibroById_Encontrado() {
        when(copiaLibroRepository.findById(1)).thenReturn(Optional.of(copia1));

        CopiaLibro resultado = copiaLibroService.getCopiaLibroById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(copiaLibroRepository, times(1)).findById(1);
    }

    @Test
    void testGetCopiaLibroById_NoEncontrado() {
        when(copiaLibroRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> copiaLibroService.getCopiaLibroById(99));

        assertEquals("Copia de libro no encontrada con ID: 99", exception.getMessage());
        verify(copiaLibroRepository, times(1)).findById(99);
    }

    @Test
    void testDeleteCopiaLibroById_Existente() {
        when(copiaLibroRepository.findById(1)).thenReturn(Optional.of(copia1));
        doNothing().when(copiaLibroRepository).delete(copia1);

        assertDoesNotThrow(() -> copiaLibroService.deleteCopiaLibroById(1));
        verify(copiaLibroRepository, times(1)).findById(1);
        verify(copiaLibroRepository, times(1)).delete(copia1);
    }

    @Test
    void testDeleteCopiaLibroById_NoExistente() {
        when(copiaLibroRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> copiaLibroService.deleteCopiaLibroById(99));

        assertEquals("Copia de libro no encontrada con ID: 99", exception.getMessage());
        verify(copiaLibroRepository, times(1)).findById(99);
    }

    @Test
    void testSaveOrUpdateCopiaLibro() {
        when(libroRepository.findById(1)).thenReturn(Optional.of(libro));
        when(copiaLibroRepository.save(any(CopiaLibro.class))).thenReturn(copia1);

        CopiaLibro resultado = copiaLibroService.saveOrUpdateCopiaLibro(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getLibro().getId());
        verify(libroRepository, times(1)).findById(1);
        verify(copiaLibroRepository, times(1)).save(any(CopiaLibro.class));
    }

    @Test
    void testSaveOrUpdateCopiaLibro_LibroNoExistente() {
        when(libroRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> copiaLibroService.saveOrUpdateCopiaLibro(99));

        assertEquals("Libro no encontrado con ID: 99", exception.getMessage());
        verify(libroRepository, times(1)).findById(99);
    }

    @Test
    void testGetLibrosDisponibles() {
        List<CopiaLibro> disponiblesMock = Arrays.asList(copia1, copia2);
        when(copiaLibroRepository.findLibrosDisponibles()).thenReturn(disponiblesMock);

        List<CopiaLibro> resultado = copiaLibroService.getLibrosDisponibles();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(copiaLibroRepository, times(1)).findLibrosDisponibles();
    }

    @Test
    void testGetLibrosDisponibles_NoDisponibles() {
        when(copiaLibroRepository.findLibrosDisponibles()).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> copiaLibroService.getLibrosDisponibles());

        assertTrue(exception.getMessage().contains("No hay libros disponibles en este momento"));
        verify(copiaLibroRepository, times(1)).findLibrosDisponibles();
    }

}

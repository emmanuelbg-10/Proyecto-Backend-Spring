package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Autor;
import com.emmanuel.biblioteca.repository.AutorRepository;
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
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor1;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        autor1 = new Autor();
        autor1.setId(1);
        autor1.setNombre("Emmanuel Barral Giraldo");

        autor2 = new Autor();
        autor2.setId(2);
        autor2.setNombre("Dario");
    }

    @Test
    void testGetAutores() {
        List<Autor> autoresMock = Arrays.asList(autor1, autor2);
        when(autorRepository.findAll()).thenReturn(autoresMock);

        List<Autor> resultado = autorService.getAutores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Emmanuel Barral Giraldo", resultado.get(0).getNombre());

        verify(autorRepository, times(1)).findAll();
    }

    @Test
    void testGetAutorById_Encontrado() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor1));

        Optional<Autor> resultado = autorService.getAutorById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Emmanuel Barral Giraldo", resultado.get().getNombre());

        verify(autorRepository, times(1)).findById(1);
    }

    @Test
    void testGetAutorById_NoEncontrado() {
        when(autorRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Autor> resultado = autorService.getAutorById(99);

        assertFalse(resultado.isPresent());

        verify(autorRepository, times(1)).findById(99);
    }

    @Test
    void testSaveOrUpdate() {
        when(autorRepository.save(autor1)).thenReturn(autor1);

        Autor resultado = autorService.saveOrUpdate(autor1);

        assertNotNull(resultado);
        assertEquals("Emmanuel Barral Giraldo", resultado.getNombre());

        verify(autorRepository, times(1)).save(autor1);
    }

    @Test
    void testDeleteAutorById_Existente() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor1));
        doNothing().when(autorRepository).deleteById(1);

        assertDoesNotThrow(() -> autorService.deleteAutorById(1));

        verify(autorRepository, times(1)).findById(1);
        verify(autorRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAutorById_NoExistente() {
        when(autorRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            autorService.deleteAutorById(99);
        });

        assertEquals("El autor con ID 99 no existe.", exception.getMessage());

        verify(autorRepository, times(1)).findById(99);
        verify(autorRepository, times(0)).deleteById(anyInt());
    }
}

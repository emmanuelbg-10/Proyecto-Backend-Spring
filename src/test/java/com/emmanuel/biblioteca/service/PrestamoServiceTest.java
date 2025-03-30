package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.CopiaLibro;
import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Usuario;
import com.emmanuel.biblioteca.repository.CopiaLibroRepository;
import com.emmanuel.biblioteca.repository.PrestamoRepository;
import com.emmanuel.biblioteca.repository.PrestamoRepositoryCustom;
import com.emmanuel.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private CopiaLibroRepository copiaLibroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepositoryCustom prestamoRepositoryCustom;

    @InjectMocks
    private PrestamoService prestamoService;

    private Usuario usuario;
    private CopiaLibro copiaLibro;
    private Prestamo prestamo;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("Juan Perez");

        copiaLibro = new CopiaLibro();
        copiaLibro.setId(1);
        copiaLibro.setDisponible(true);

        prestamo = new Prestamo(usuario, copiaLibro, LocalDate.now(), LocalDate.now().plusDays(14), null, false);
        prestamo.setId(1);
    }

    @Test
    void testGetPrestamos() {
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        List<Prestamo> prestamos = prestamoService.getPrestamos();

        assertFalse(prestamos.isEmpty());
        assertEquals(1, prestamos.size());
        verify(prestamoRepository, times(1)).findAll();
    }

    @Test
    void testGetPrestamoById_Found() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        Prestamo result = prestamoService.getPrestamoById(1);

        assertNotNull(result);
        assertEquals(prestamo.getId(), result.getId());
        verify(prestamoRepository, times(1)).findById(1);
    }

    @Test
    void testGetPrestamoById_NotFound() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> prestamoService.getPrestamoById(1));
        assertEquals("No se encontró el préstamo con ID 1", exception.getMessage());
        verify(prestamoRepository, times(1)).findById(1);
    }

    @Test
    void testCreatePrestamo_Success() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(copiaLibroRepository.findById(1)).thenReturn(Optional.of(copiaLibro));
        when(prestamoRepository.existsByCopiaLibroAndDevueltoFalse(copiaLibro)).thenReturn(false);
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamo);

        Prestamo nuevoPrestamo = prestamoService.createPrestamo(1, 1);

        assertNotNull(nuevoPrestamo);
        assertEquals(usuario, nuevoPrestamo.getUsuario());
        assertEquals(copiaLibro, nuevoPrestamo.getCopiaLibro());
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void testCreatePrestamo_CopiaNoDisponible() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(copiaLibroRepository.findById(1)).thenReturn(Optional.of(copiaLibro));
        when(prestamoRepository.existsByCopiaLibroAndDevueltoFalse(copiaLibro)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> prestamoService.createPrestamo(1, 1));
        assertEquals("Esta copia del libro ya está prestada", exception.getMessage());
    }

    @Test
    void testDeletePrestamo_Success() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        prestamoService.deletePrestamo(1, 1);

        verify(prestamoRepository, times(1)).delete(prestamo);
    }

    @Test
    void testDeletePrestamo_NotFound() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> prestamoService.deletePrestamo(1, 1));
        assertEquals("Préstamo no encontrado", exception.getMessage());
    }

    @Test
    void testGetUsuariosConMasPrestamosUltimoAnio_Valid() {
        List<Object[]> mockData = List.of(new Object[][]{{"Usuario1", 10}});  // Corrección aquí

        when(prestamoRepositoryCustom.getUsuariosConMasPrestamosUltimoAnio(5))
                .thenReturn(mockData);

        List<Object[]> resultado = prestamoService.getUsuariosConMasPrestamosUltimoAnio(5);

        assertFalse(resultado.isEmpty());
        verify(prestamoRepositoryCustom, times(1)).getUsuariosConMasPrestamosUltimoAnio(5);
    }


    @Test
    void testGetUsuariosConMasPrestamosUltimoAnio_NoResultados() {
        when(prestamoRepositoryCustom.getUsuariosConMasPrestamosUltimoAnio(5)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> prestamoService.getUsuariosConMasPrestamosUltimoAnio(5));

        assertEquals("Error al obtener los usuarios con más préstamos: No se encontraron usuarios con préstamos en el último año",
                exception.getMessage());
    }
}

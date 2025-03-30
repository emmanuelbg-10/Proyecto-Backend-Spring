package com.emmanuel.biblioteca.configuration;

import com.emmanuel.biblioteca.entity.*;
import com.emmanuel.biblioteca.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner usuarioDataLoader(UsuarioRepository usuarioRepository, LibroRepository libroRepository, ResenaRepository resenaRepository, CopiaLibroRepository copiaLibroRepository, PrestamoRepository prestamoRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // Crear usuarios
                Usuario usuario1 = new Usuario("emmanuel", "emmanuel@gmail.com", "contrasena1", 111111111L, "AUTOR");
                Usuario usuario2 = new Usuario("john", "john@example.com", "contrasena2", 222222222L, "USUARIO");
                Usuario usuario3 = new Usuario("mary", "mary@example.com", "contrasena3", 333333333L, "USUARIO");
                Usuario usuario4 = new Usuario("lucas", "lucas@example.com", "contrasena4", 444444444L, "USUARIO");
                Usuario usuario5 = new Usuario("Gabriel García Márquez", "ggm@example.com", "contrasena1", 111111112L, "AUTOR");
                Usuario usuario6 = new Usuario("Jane Austen", "jausten@example.com", "contrasena2", 222222223L, "AUTOR");
                Usuario usuario7 = new Usuario("George Orwell", "gorwell@example.com", "contrasena3", 333333334L, "AUTOR");
                Usuario usuario8 = new Usuario("J.K. Rowling", "jkrowling@example.com", "contrasena4", 444444445L, "AUTOR");
                usuarioRepository.saveAll(List.of(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6, usuario7, usuario8));

                // Crear libros para los autores
                Libro libro1 = new Libro("Cien años de soledad", "Realismo mágico", usuario5,"https://imagessl4.casadellibro.com/a/l/t0/64/9788439731764.jpg");
                Libro libro2 = new Libro("Orgullo y prejuicio", "Romance", usuario6, "https://th.bing.com/th/id/OIP.jWGnnNreh9BPP1ACbwhi0AHaK-?rs=1&pid=ImgDetMain");
                Libro libro3 = new Libro("Harry Potter y la piedra filosofal", "Fantástico", usuario8, "https://i0.wp.com/www.epubgratis.org/wp-content/uploads/2012/04/Harry-Potter-y-la-Piedra-Filosofal-J.K.-Rowling-portada.jpg?fit=683%2C1024&ssl=1");
                libroRepository.saveAll(List.of(libro1, libro2, libro3));



                // Crear reseñas
                Resena resena1 = new Resena(libro1, usuario1, 5, "Excelente libro, una obra maestra.");
                Resena resena2 = new Resena(libro2, usuario2, 4, "Muy buen libro, aunque un poco largo.");
                Resena resena3 = new Resena(libro3, usuario3, 5, "Maravillosa historia de amor.");
                resenaRepository.saveAll(List.of(resena1, resena2, resena3));

                // Crear copias de libros
                CopiaLibro copiaLibro1 = new CopiaLibro(libro1);
                CopiaLibro copiaLibro2 = new CopiaLibro(libro2);
                CopiaLibro copiaLibro3 = new CopiaLibro(libro3);
                copiaLibroRepository.saveAll(List.of(copiaLibro1, copiaLibro2, copiaLibro3));

                // Crear préstamos
                LocalDate fechaInicio = LocalDate.now();
                LocalDate fechaVencimiento = fechaInicio.plusWeeks(2);
                Prestamo prestamo1 = new Prestamo(usuario1, copiaLibro1, fechaInicio, fechaVencimiento, null, false);
                Prestamo prestamo2 = new Prestamo(usuario2, copiaLibro2, fechaInicio, fechaVencimiento, null, false);
                Prestamo prestamo3 = new Prestamo(usuario3, copiaLibro3, fechaInicio, fechaVencimiento, null, false);
                prestamoRepository.saveAll(List.of(prestamo1, prestamo2, prestamo3));

                System.out.println("Datos falsos insertados correctamente.");
            }
        };
    }
}


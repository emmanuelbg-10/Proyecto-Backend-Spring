package com.emmanuel.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Controla la serialización evitando el bucle
    private List<Libro> libros;

    // Constructor vacío (necesario para JPA)
    public Autor() {}

    // Constructor con parámetros
    public Autor(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

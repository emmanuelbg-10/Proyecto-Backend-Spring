package com.emmanuel.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 2, max = 200, message = "El título debe tener entre 2 y 200 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El género no puede estar vacío")
    @Size(min = 2, max = 100, message = "El género debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String genero;

    @NotBlank(message = "El código único no puede estar vacío")
    @Size(min = 5, max = 50, message = "El código único debe tener entre 5 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$", message = "El código único solo puede contener letras, números y guiones")
    @Column(name = "codigo_unico", unique = true, nullable = false)
    private String codigoUnico;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CopiaLibro> copiaLibros;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("libro-resena")
    private List<Resena> resenas;

    public Libro() {}

    public Libro(String titulo, String genero, String codigoUnico, Autor autor) {
        this.titulo = titulo;
        this.genero = genero;
        this.codigoUnico = codigoUnico;
        this.autor = autor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<CopiaLibro> getCopiaLibros() {
        return copiaLibros;
    }

    public void setCopiaLibros(List<CopiaLibro> copiaLibros) {
        this.copiaLibros = copiaLibros;
    }

    public List<Resena> getResenas() {
        return resenas;
    }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", autor=" + autor +
                ", copiaLibros=" + copiaLibros +
                ", resenas=" + resenas +
                '}';
    }
}

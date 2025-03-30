package com.emmanuel.biblioteca.entity;

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

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CopiaLibro> copiaLibros;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("libro-resena")
    private List<Resena> resenas;

    public Libro() {}

    public Libro(String titulo, String genero, Usuario usuario, String imagenUrl) {
        this.titulo = titulo;
        this.genero = genero;
        this.usuario = usuario;
        this.imagenUrl = imagenUrl;
    }

    public Libro(String titulo, String genero, Usuario usuario) {
        this.titulo = titulo;
        this.genero = genero;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
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
                ", autor=" + usuario +
                ", copiaLibros=" + copiaLibros +
                ", resenas=" + resenas +
                '}';
    }
}

package com.emmanuel.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Column(unique = true, nullable = false)
    private String correo;

    @Digits(integer = 9, fraction = 0, message = "El teléfono debe contener hasta 9 dígitos")
    @Column(unique = true, nullable = false)
    private Long telefono;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Resena> resenas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prestamo> prestamos;

    public Usuario() {}

    public Usuario(String nombre, String correo, Long telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public List<Resena> getResenas() {
        return resenas;
    }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono=" + telefono +
                ", resenas=" + resenas +
                ", prestamos=" + prestamos +
                '}';
    }
}

package com.emmanuel.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class CopiaLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @OneToMany(mappedBy = "copiaLibro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("copiaLibro-prestamos")
    private List<Prestamo> prestamos;

    @Column(nullable = false)
    private boolean disponible = true; // 🔹 Indica si la copia está prestada

    public CopiaLibro() {}

    public CopiaLibro(Libro libro) {
        this.libro = libro;
    }

    public @NotNull(message = "El estado de préstamo no puede ser nulo") boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(@NotNull(message = "El estado de préstamo no puede ser nulo") boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "CopiaLibro{" +
                "id=" + id +
                ", libro=" + libro +
                ", prestamos=" + prestamos +
                ", disponible=" + disponible +
                '}';
    }
}

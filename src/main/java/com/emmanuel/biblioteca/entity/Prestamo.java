package com.emmanuel.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El usuario no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-prestamo")
    private Usuario usuario;

    @NotNull(message = "La copia del libro no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "copia_libro_id", nullable = false)
    @JsonBackReference("copiaLibro-prestamos")
    private CopiaLibro copiaLibro;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o en el futuro")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de vencimiento no puede ser nula")
    @Future(message = "La fecha de vencimiento debe estar en el futuro")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @PastOrPresent(message = "La fecha de devolución debe ser hoy o en el pasado")
    @Column(name = "fecha_devolucion", nullable = true)
    private LocalDate fechaDevolucion;

    @Column(nullable = false)
    private boolean devuelto = false; // Por defecto, el préstamo no está devuelto

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Prestamo() {}

    public Prestamo(Usuario usuario, CopiaLibro copiaLibro, LocalDate fechaInicio, LocalDate fechaVencimiento, LocalDate fechaDevolucion, boolean devuelto) {
        this.usuario = usuario;
        this.copiaLibro = copiaLibro;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = fechaDevolucion != null ? fechaDevolucion : null;
        this.devuelto = devuelto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CopiaLibro getCopiaLibro() {
        return copiaLibro;
    }

    public void setCopiaLibro(CopiaLibro copiaLibro) {
        this.copiaLibro = copiaLibro;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", copiaLibro=" + copiaLibro +
                ", fechaInicio=" + fechaInicio +
                ", fechaVencimiento=" + fechaVencimiento +
                ", fechaDevolucion=" + fechaDevolucion +
                ", devuelto=" + devuelto +
                '}';
    }
}

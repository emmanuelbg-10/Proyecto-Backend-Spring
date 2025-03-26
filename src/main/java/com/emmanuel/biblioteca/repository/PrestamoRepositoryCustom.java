package com.emmanuel.biblioteca.repository;

import com.emmanuel.biblioteca.entity.Prestamo;
import com.emmanuel.biblioteca.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PrestamoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> getUsuariosConMasPrestamosUltimoAnio(int maxResultados) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Prestamo> prestamo = query.from(Prestamo.class);
        Join<Prestamo, Usuario> usuario = prestamo.join("usuario");

        // Obtener la fecha de hace 1 año
        LocalDate fechaHaceUnAnio = LocalDate.now().minusYears(1);

        // Seleccionar el usuario y contar sus préstamos en el último año
        query.multiselect(
                        usuario.get("id"),
                        usuario.get("nombre"),
                        cb.count(prestamo.get("id"))
                )
                .where(cb.greaterThanOrEqualTo(prestamo.get("fechaInicio"), fechaHaceUnAnio))
                .groupBy(usuario.get("id"), usuario.get("nombre"))
                .orderBy(cb.desc(cb.count(prestamo.get("id"))));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(maxResultados); // Limitar resultados si se necesita

        return typedQuery.getResultList();
    }

    public int countUsuariosConPrestamosUltimoAnio() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Prestamo> prestamo = query.from(Prestamo.class);

        Join<Prestamo, Usuario> usuario = prestamo.join("usuario");

        LocalDate fechaHaceUnAnio = LocalDate.now().minusYears(1);

        query.select(cb.countDistinct(usuario.get("id")))
                .where(cb.greaterThanOrEqualTo(prestamo.get("fechaInicio"), fechaHaceUnAnio));

        return entityManager.createQuery(query).getSingleResult().intValue();
    }

}

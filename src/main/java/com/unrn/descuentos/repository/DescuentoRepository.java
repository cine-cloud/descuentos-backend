package com.unrn.descuentos.repository;

import com.unrn.descuentos.domain.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {

    @Query("SELECT d FROM Descuento d WHERE d.fechaDesde <= :fecha AND d.fechaHasta >= :fecha")
    List<Descuento> findActiveDiscounts(@Param("fecha") LocalDate fecha);

    Optional<Descuento> findByCodigo(String codigo);
}

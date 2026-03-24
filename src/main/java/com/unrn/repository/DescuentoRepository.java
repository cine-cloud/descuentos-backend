package com.unrn.repository;

import com.unrn.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
    
    // Encuentra los descuentos que están activos en la fecha indicada
    List<Descuento> findByActivoTrueAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            LocalDateTime fechaActualV1, LocalDateTime fechaActualV2);

}

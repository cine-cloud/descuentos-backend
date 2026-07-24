package com.unrn.descuentos.repository;

import com.unrn.descuentos.domain.Descuento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DescuentoRepositoryTest {

    @Autowired
    private DescuentoRepository repository;

    @Test
    @DisplayName("Guardar un descuento y recuperarlo por ID funciona correctamente")
    void guardarYRecuperarDescuento_funcionaCorrectamente() {
        // Setup: Preparar el escenario
        Descuento descuento = new Descuento("CINE50", "50 pesos de descuento", new BigDecimal("50.00"), LocalDate.now(), LocalDate.now().plusDays(5));

        // Ejercitación: Ejecutar la acción a probar
        Descuento saved = repository.save(descuento);
        Optional<Descuento> found = repository.findById(saved.id());

        // Verificación: Verificar el resultado esperado
        assertTrue(found.isPresent(), "El descuento debería haberse guardado y encontrado");
        assertEquals("CINE50", found.get().codigo(), "El código del descuento encontrado debe coincidir");
    }

    @Test
    @DisplayName("Buscar descuentos activos devuelve solo los descuentos dentro del rango de vigencia")
    void buscarDescuentosActivos_devuelveSoloVigentes() {
        // Setup: Preparar el escenario
        Descuento activo = new Descuento("ACTIVO", "Descuento Activo", new BigDecimal("10.00"), LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Descuento futuro = new Descuento("FUTURO", "Descuento Futuro", new BigDecimal("20.00"), LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));
        Descuento pasado = new Descuento("PASADO", "Descuento Pasado", new BigDecimal("30.00"), LocalDate.now().minusDays(5), LocalDate.now().minusDays(2));

        repository.save(activo);
        repository.save(futuro);
        repository.save(pasado);

        // Ejercitación: Ejecutar la acción a probar
        List<Descuento> activos = repository.findActiveDiscounts(LocalDate.now());

        // Verificación: Verificar el resultado esperado
        assertEquals(1, activos.size(), "Debería haber exactamente 1 descuento activo");
        assertEquals("ACTIVO", activos.get(0).codigo(), "El código del descuento activo debe ser 'ACTIVO'");
    }
}

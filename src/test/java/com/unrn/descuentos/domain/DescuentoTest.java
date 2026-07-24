package com.unrn.descuentos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DescuentoTest {

    @Test
    @DisplayName("Crear descuento con valores válidos se instancia correctamente")
    void crearDescuentoConValoresValidos_seInstanciaCorrectamente() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación: Ejecutar la acción a probar
        Descuento descuento = new Descuento(codigo, descripcion, monto, desde, hasta);

        // Verificación: Verificar el resultado esperado
        assertNull(descuento.id(), "El ID inicial debe ser nulo");
        assertEquals(codigo, descuento.codigo(), "El código debe coincidir");
        assertEquals(descripcion, descuento.descripcion(), "La descripción debe coincidir");
        assertEquals(monto, descuento.monto(), "El monto debe coincidir");
        assertEquals(desde, descuento.fechaDesde(), "La fecha de inicio debe coincidir");
        assertEquals(hasta, descuento.fechaHasta(), "La fecha de fin debe coincidir");
    }

    @Test
    @DisplayName("Crear descuento con código nulo lanza excepción de código vacío")
    void crearDescuentoConCodigoNulo_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = null;
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por código nulo");

        assertEquals(Descuento.ERROR_CODIGO_VACIO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con código vacío lanza excepción de código vacío")
    void crearDescuentoConCodigoVacio_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "   ";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por código vacío");

        assertEquals(Descuento.ERROR_CODIGO_VACIO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con descripción nula lanza excepción de descripción vacía")
    void crearDescuentoConDescripcionNula_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = null;
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por descripción nula");

        assertEquals(Descuento.ERROR_DESCRIPCION_VACIA, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con descripción vacía lanza excepción de descripción vacía")
    void crearDescuentoConDescripcionVacia_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por descripción vacía");

        assertEquals(Descuento.ERROR_DESCRIPCION_VACIA, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con monto nulo lanza excepción de monto inválido")
    void crearDescuentoConMontoNulo_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = null;
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por monto nulo");

        assertEquals(Descuento.ERROR_MONTO_INVALIDO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con monto menor a cero lanza excepción de monto inválido")
    void crearDescuentoConMontoMenorACero_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("-5.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por monto menor a cero");

        assertEquals(Descuento.ERROR_MONTO_INVALIDO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con monto igual a cero lanza excepción de monto inválido")
    void crearDescuentoConMontoCero_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = BigDecimal.ZERO;
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por monto igual a cero");

        assertEquals(Descuento.ERROR_MONTO_INVALIDO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con fecha desde nula lanza excepción")
    void crearDescuentoConFechaDesdeNula_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = null;
        LocalDate hasta = LocalDate.now().plusDays(10);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por fecha desde nula");

        assertEquals(Descuento.ERROR_FECHA_DESDE_NULA, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con fecha hasta nula lanza excepción")
    void crearDescuentoConFechaHastaNula_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = null;

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por fecha hasta nula");

        assertEquals(Descuento.ERROR_FECHA_HASTA_NULA, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Crear descuento con fecha hasta anterior a fecha desde lanza excepción de rango de fechas inválido")
    void crearDescuentoConRangoFechasInvalido_lanzaExcepcion() {
        // Setup: Preparar el escenario
        String codigo = "PROMO10";
        String descripcion = "10% de descuento";
        BigDecimal monto = new BigDecimal("150.00");
        LocalDate desde = LocalDate.now();
        LocalDate hasta = LocalDate.now().minusDays(1);

        // Ejercitación y Verificación
        var ex = assertThrows(RuntimeException.class, () -> {
            new Descuento(codigo, descripcion, monto, desde, hasta);
        }, "Debería lanzar RuntimeException por fecha hasta anterior a desde");

        assertEquals(Descuento.ERROR_RANGO_FECHAS_INVALIDO, ex.getMessage(), "El mensaje de error debe coincidir con la constante");
    }

    @Test
    @DisplayName("Modificar descuento con valores válidos actualiza atributos correctamente")
    void modificarDescuentoConValoresValidos_actualizaAtributosCorrectamente() {
        // Setup: Preparar el escenario
        Descuento descuento = new Descuento("PROMO10", "10%", new BigDecimal("100.00"), LocalDate.now(), LocalDate.now().plusDays(5));
        String nuevoCodigo = "PROMO20";
        String nuevaDesc = "20%";
        BigDecimal nuevoMonto = new BigDecimal("200.00");
        LocalDate nuevoDesde = LocalDate.now().plusDays(1);
        LocalDate nuevoHasta = LocalDate.now().plusDays(15);

        // Ejercitación: Ejecutar la acción a probar
        descuento.modificar(nuevoCodigo, nuevaDesc, nuevoMonto, nuevoDesde, nuevoHasta);

        // Verificación: Verificar el resultado esperado
        assertEquals(nuevoCodigo, descuento.codigo(), "El código debe haberse modificado");
        assertEquals(nuevaDesc, descuento.descripcion(), "La descripción debe haberse modificada");
        assertEquals(nuevoMonto, descuento.monto(), "El monto debe haberse modificado");
        assertEquals(nuevoDesde, descuento.fechaDesde(), "La fecha de inicio debe haberse modificada");
        assertEquals(nuevoHasta, descuento.fechaHasta(), "La fecha de fin debe haberse modificada");
    }
}

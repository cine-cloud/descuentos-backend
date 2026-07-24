package com.unrn.descuentos.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "descuentos")
public class Descuento {

    static final String ERROR_CODIGO_VACIO = "El código de descuento no puede estar vacío";
    static final String ERROR_DESCRIPCION_VACIA = "La descripción no puede estar vacía";
    static final String ERROR_MONTO_INVALIDO = "El monto a descontar debe ser mayor a cero";
    static final String ERROR_FECHA_DESDE_NULA = "La fecha de inicio no puede ser nula";
    static final String ERROR_FECHA_HASTA_NULA = "La fecha de fin no puede ser nula";
    static final String ERROR_RANGO_FECHAS_INVALIDO = "La fecha de fin debe ser posterior o igual a la fecha de inicio";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "descuento_id")
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_desde", nullable = false)
    private LocalDate fechaDesde;

    @Column(name = "fecha_hasta", nullable = false)
    private LocalDate fechaHasta;

    protected Descuento() {}

    public Descuento(String codigo, String descripcion, BigDecimal monto, LocalDate fechaDesde, LocalDate fechaHasta) {
        assertCodigoValido(codigo);
        assertDescripcionValida(descripcion);
        assertMontoValido(monto);
        assertFechasValidas(fechaDesde, fechaHasta);

        this.codigo = codigo.trim();
        this.descripcion = descripcion.trim();
        this.monto = monto;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public void modificar(String codigo, String descripcion, BigDecimal monto, LocalDate fechaDesde, LocalDate fechaHasta) {
        assertCodigoValido(codigo);
        assertDescripcionValida(descripcion);
        assertMontoValido(monto);
        assertFechasValidas(fechaDesde, fechaHasta);

        this.codigo = codigo.trim();
        this.descripcion = descripcion.trim();
        this.monto = monto;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    private void assertCodigoValido(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException(ERROR_CODIGO_VACIO);
        }
    }

    private void assertDescripcionValida(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new RuntimeException(ERROR_DESCRIPCION_VACIA);
        }
    }

    private void assertMontoValido(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(ERROR_MONTO_INVALIDO);
        }
    }

    private void assertFechasValidas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde == null) {
            throw new RuntimeException(ERROR_FECHA_DESDE_NULA);
        }
        if (fechaHasta == null) {
            throw new RuntimeException(ERROR_FECHA_HASTA_NULA);
        }
        if (fechaHasta.isBefore(fechaDesde)) {
            throw new RuntimeException(ERROR_RANGO_FECHAS_INVALIDO);
        }
    }

    public Integer id() {
        return id;
    }

    // Para permitir setear el ID en tests de integración / Mockito si es necesario
    public void setIdForTesting(Integer id) {
        this.id = id;
    }

    public String codigo() {
        return codigo;
    }

    public String descripcion() {
        return descripcion;
    }

    public BigDecimal monto() {
        return monto;
    }

    public LocalDate fechaDesde() {
        return fechaDesde;
    }

    public LocalDate fechaHasta() {
        return fechaHasta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Descuento descuento = (Descuento) o;
        return Objects.equals(id, descuento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

package com.unrn.descuentos.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoSimplificado {
    private Integer id;
    private String codigo;
    private BigDecimal monto;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
}

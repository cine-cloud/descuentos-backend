package com.unrn.descuentos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DescuentoDTO {
    private Integer id;
    private String codigo;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
}

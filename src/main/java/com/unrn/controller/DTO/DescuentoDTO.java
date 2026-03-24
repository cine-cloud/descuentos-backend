package com.unrn.controller.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DescuentoDTO {
    private Long id;
    private String titulo;
    private BigDecimal montoDescuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
}

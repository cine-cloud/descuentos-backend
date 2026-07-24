package com.unrn.descuentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrn.descuentos.dto.DescuentoDTO;
import com.unrn.descuentos.service.DescuentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.unrn.descuentos.config.SecurityConfig;

@WebMvcTest(
    controllers = DescuentoController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    },
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    )
)
class DescuentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DescuentoService descuentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /descuentos crea un descuento y devuelve 200 OK")
    void crearDescuento_creaYRetornaDTO() throws Exception {
        // Setup: Preparar el escenario
        DescuentoDTO inputDto = DescuentoDTO.builder()
                .codigo("PROMO10")
                .descripcion("10% descuento")
                .monto(new BigDecimal("100.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(5))
                .build();

        DescuentoDTO outputDto = DescuentoDTO.builder()
                .id(1)
                .codigo("PROMO10")
                .descripcion("10% descuento")
                .monto(new BigDecimal("100.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(5))
                .build();

        when(descuentoService.crearDescuento(any(DescuentoDTO.class))).thenReturn(outputDto);

        // Ejercitación y Verificación
        mockMvc.perform(post("/descuentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("PROMO10"))
                .andExpect(jsonPath("$.monto").value(100.00));
    }

    @Test
    @DisplayName("PUT /descuentos/{id} edita un descuento existente y devuelve DTO actualizado")
    void editarDescuento_editaYRetornaDTO() throws Exception {
        // Setup: Preparar el escenario
        DescuentoDTO inputDto = DescuentoDTO.builder()
                .codigo("PROMO20")
                .descripcion("20% descuento")
                .monto(new BigDecimal("200.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(10))
                .build();

        DescuentoDTO outputDto = DescuentoDTO.builder()
                .id(1)
                .codigo("PROMO20")
                .descripcion("20% descuento")
                .monto(new BigDecimal("200.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(10))
                .build();

        when(descuentoService.editarDescuento(eq(1), any(DescuentoDTO.class))).thenReturn(outputDto);

        // Ejercitación y Verificación
        mockMvc.perform(put("/descuentos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("PROMO20"))
                .andExpect(jsonPath("$.monto").value(200.00));
    }

    @Test
    @DisplayName("DELETE /descuentos/{id} elimina el descuento y devuelve 200 OK")
    void eliminarDescuento_eliminaCorrectamente() throws Exception {
        // Setup: Preparar el escenario
        doNothing().when(descuentoService).eliminarDescuento(1);

        // Ejercitación y Verificación
        mockMvc.perform(delete("/descuentos/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /descuentos/{id} obtiene el detalle de un descuento")
    void obtenerDetalleDescuento_retornaDTO() throws Exception {
        // Setup: Preparar el escenario
        DescuentoDTO dto = DescuentoDTO.builder()
                .id(1)
                .codigo("PROMO10")
                .descripcion("10% descuento")
                .monto(new BigDecimal("100.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(5))
                .build();

        when(descuentoService.obtenerDescuentoPorId(1)).thenReturn(dto);

        // Ejercitación y Verificación
        mockMvc.perform(get("/descuentos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("PROMO10"));
    }

    @Test
    @DisplayName("GET /descuentos obtiene la lista de descuentos activos")
    void listarDescuentosActivos_retornaLista() throws Exception {
        // Setup: Preparar el escenario
        DescuentoDTO dto = DescuentoDTO.builder()
                .id(1)
                .codigo("PROMO10")
                .descripcion("10% descuento")
                .monto(new BigDecimal("100.00"))
                .build();

        when(descuentoService.listarDescuentosActivos()).thenReturn(Arrays.asList(dto));

        // Ejercitación y Verificación
        mockMvc.perform(get("/descuentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].codigo").value("PROMO10"));
    }
}

package com.unrn.descuentos.service;

import com.unrn.descuentos.domain.Descuento;
import com.unrn.descuentos.dto.DescuentoDTO;
import com.unrn.descuentos.event.EventType;
import com.unrn.descuentos.event.dto.Event;
import com.unrn.descuentos.repository.DescuentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DescuentoServiceTest {

    @Mock
    private DescuentoRepository repository;

    @Mock
    private DescuentoEventPublisher eventPublisher;

    @InjectMocks
    private DescuentoService service;

    private DescuentoDTO dto;
    private Descuento descuento;

    @BeforeEach
    void setUp() {
        dto = DescuentoDTO.builder()
                .codigo("PROMO15")
                .descripcion("15% de descuento")
                .monto(new BigDecimal("150.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(10))
                .build();

        descuento = new Descuento(
                dto.getCodigo(),
                dto.getDescripcion(),
                dto.getMonto(),
                dto.getFechaDesde(),
                dto.getFechaHasta()
        );
        descuento.setIdForTesting(1);
    }

    @Test
    @DisplayName("Crear descuento con datos válidos guarda en repositorio y publica evento")
    void crearDescuentoValido_guardaYPublicaEvento() {
        // Setup: Preparar el escenario
        when(repository.save(any(Descuento.class))).thenAnswer(invocation -> {
            Descuento d = invocation.getArgument(0);
            d.setIdForTesting(1);
            return d;
        });

        // Ejercitación: Ejecutar la acción a probar
        DescuentoDTO result = service.crearDescuento(dto);

        // Verificación: Verificar el resultado esperado
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals(1, result.getId(), "El ID debería coincidir con el asignado por base de datos");
        assertEquals("PROMO15", result.getCodigo(), "El código debería coincidir");

        verify(repository, times(1)).save(any(Descuento.class));
        verify(eventPublisher, times(1)).enviarEvento(any(Event.class));
    }

    @Test
    @DisplayName("Editar descuento existente modifica los datos y publica evento de actualización")
    void editarDescuentoExistente_modificaYPublicaEvento() {
        // Setup: Preparar el escenario
        when(repository.findById(1)).thenReturn(Optional.of(descuento));
        when(repository.save(any(Descuento.class))).thenReturn(descuento);

        DescuentoDTO editDto = DescuentoDTO.builder()
                .codigo("PROMO20")
                .descripcion("20% de descuento")
                .monto(new BigDecimal("200.00"))
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(15))
                .build();

        // Ejercitación: Ejecutar la acción a probar
        DescuentoDTO result = service.editarDescuento(1, editDto);

        // Verificación: Verificar el resultado esperado
        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals("PROMO20", result.getCodigo(), "El código editado debería coincidir");
        assertEquals(new BigDecimal("200.00"), result.getMonto(), "El monto editado debe coincidir");

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Descuento.class));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventPublisher, times(1)).enviarEvento(eventCaptor.capture());
        assertEquals(EventType.UPDATE, eventCaptor.getValue().getEventType(), "El evento publicado debería ser UPDATE");
    }

    @Test
    @DisplayName("Editar descuento inexistente lanza excepción de no encontrado")
    void editarDescuentoInexistente_lanzaExcepcionNoEncontrado() {
        // Setup: Preparar el escenario
        when(repository.findById(99)).thenReturn(Optional.empty());

        // Ejercitación y Verificación
        assertThrows(ResponseStatusException.class, () -> {
            service.editarDescuento(99, dto);
        }, "Debería lanzar ResponseStatusException al no encontrar el descuento");

        verify(repository, times(1)).findById(99);
        verify(repository, never()).save(any(Descuento.class));
        verify(eventPublisher, never()).enviarEvento(any(Event.class));
    }

    @Test
    @DisplayName("Eliminar descuento existente borra de base de datos y publica evento de borrado")
    void eliminarDescuentoExistente_borraYPublicaEvento() {
        // Setup: Preparar el escenario
        when(repository.findById(1)).thenReturn(Optional.of(descuento));

        // Ejercitación: Ejecutar la acción a probar
        service.eliminarDescuento(1);

        // Verificación: Verificar el resultado esperado
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(descuento);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventPublisher, times(1)).enviarEvento(eventCaptor.capture());
        assertEquals(EventType.DELETE, eventCaptor.getValue().getEventType(), "El evento publicado debería ser DELETE");
    }

    @Test
    @DisplayName("Eliminar descuento inexistente lanza excepción de no encontrado")
    void eliminarDescuentoInexistente_lanzaExcepcionNoEncontrado() {
        // Setup: Preparar el escenario
        when(repository.findById(99)).thenReturn(Optional.empty());

        // Ejercitación y Verificación
        assertThrows(ResponseStatusException.class, () -> {
            service.eliminarDescuento(99);
        }, "Debería lanzar ResponseStatusException al intentar eliminar descuento inexistente");

        verify(repository, times(1)).findById(99);
        verify(repository, never()).delete(any(Descuento.class));
        verify(eventPublisher, never()).enviarEvento(any(Event.class));
    }

    @Test
    @DisplayName("Obtener detalle de descuento por ID devuelve DTO si existe")
    void obtenerDescuentoPorIdExistente_devuelveDTO() {
        // Setup: Preparar el escenario
        when(repository.findById(1)).thenReturn(Optional.of(descuento));

        // Ejercitación: Ejecutar la acción a probar
        DescuentoDTO result = service.obtenerDescuentoPorId(1);

        // Verificación: Verificar el resultado esperado
        assertNotNull(result, "El DTO devuelto no debería ser nulo");
        assertEquals(1, result.getId(), "El ID devuelto debe coincidir");
        assertEquals("PROMO15", result.getCodigo(), "El código debe coincidir");
    }

    @Test
    @DisplayName("Obtener detalle de descuento inexistente lanza excepción de no encontrado")
    void obtenerDescuentoPorIdInexistente_lanzaExcepcionNoEncontrado() {
        // Setup: Preparar el escenario
        when(repository.findById(99)).thenReturn(Optional.empty());

        // Ejercitación y Verificación
        assertThrows(ResponseStatusException.class, () -> {
            service.obtenerDescuentoPorId(99);
        }, "Debería lanzar ResponseStatusException al buscar descuento inexistente");
    }

    @Test
    @DisplayName("Listar descuentos activos retorna lista de DTOs vigentes")
    void listarDescuentosActivos_retornaListaDTO() {
        // Setup: Preparar el escenario
        when(repository.findActiveDiscounts(any(LocalDate.class))).thenReturn(List.of(descuento));

        // Ejercitación: Ejecutar la acción a probar
        List<DescuentoDTO> result = service.listarDescuentosActivos();

        // Verificación: Verificar el resultado esperado
        assertEquals(1, result.size(), "La lista debería contener 1 elemento");
        assertEquals("PROMO15", result.get(0).getCodigo(), "El código del descuento listado debe coincidir");
    }
}

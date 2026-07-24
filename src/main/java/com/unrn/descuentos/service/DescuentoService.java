package com.unrn.descuentos.service;

import com.unrn.descuentos.domain.Descuento;
import com.unrn.descuentos.dto.DescuentoDTO;
import com.unrn.descuentos.event.EventType;
import com.unrn.descuentos.event.dto.DescuentoSimplificado;
import com.unrn.descuentos.event.dto.Event;
import com.unrn.descuentos.repository.DescuentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final DescuentoEventPublisher eventPublisher;

    public DescuentoService(DescuentoRepository descuentoRepository, DescuentoEventPublisher eventPublisher) {
        this.descuentoRepository = descuentoRepository;
        this.eventPublisher = eventPublisher;
    }

    public DescuentoDTO crearDescuento(DescuentoDTO dto) {
        Descuento descuento = new Descuento(
                dto.getCodigo(),
                dto.getDescripcion(),
                dto.getMonto(),
                dto.getFechaDesde(),
                dto.getFechaHasta()
        );

        Descuento savedDescuento = descuentoRepository.save(descuento);

        // Publish CREATE event
        publicarEvento(EventType.CREATE, savedDescuento);

        return toDTO(savedDescuento);
    }

    public DescuentoDTO editarDescuento(Integer id, DescuentoDTO dto) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado"));

        descuento.modificar(
                dto.getCodigo(),
                dto.getDescripcion(),
                dto.getMonto(),
                dto.getFechaDesde(),
                dto.getFechaHasta()
        );

        Descuento updatedDescuento = descuentoRepository.save(descuento);

        // Publish UPDATE event
        publicarEvento(EventType.UPDATE, updatedDescuento);

        return toDTO(updatedDescuento);
    }

    public void eliminarDescuento(Integer id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado"));

        descuentoRepository.delete(descuento);

        // Publish DELETE event
        publicarEvento(EventType.DELETE, descuento);
    }

    @Transactional(readOnly = true)
    public DescuentoDTO obtenerDescuentoPorId(Integer id) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado"));
        return toDTO(descuento);
    }

    @Transactional(readOnly = true)
    public List<DescuentoDTO> listarDescuentosActivos() {
        return descuentoRepository.findActiveDiscounts(LocalDate.now()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DescuentoDTO> listarTodosLosDescuentos() {
        return descuentoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void publicarEvento(EventType eventType, Descuento descuento) {
        DescuentoSimplificado simplificado = new DescuentoSimplificado(
                descuento.id(),
                descuento.codigo(),
                descuento.monto(),
                descuento.fechaDesde(),
                descuento.fechaHasta()
        );
        Event<Integer, DescuentoSimplificado> event = new Event<>(eventType, descuento.id(), simplificado);
        eventPublisher.enviarEvento(event);
    }

    private DescuentoDTO toDTO(Descuento descuento) {
        return DescuentoDTO.builder()
                .id(descuento.id())
                .codigo(descuento.codigo())
                .descripcion(descuento.descripcion())
                .monto(descuento.monto())
                .fechaDesde(descuento.fechaDesde())
                .fechaHasta(descuento.fechaHasta())
                .build();
    }
}

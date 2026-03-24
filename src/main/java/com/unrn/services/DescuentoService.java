package com.unrn.services;

import com.unrn.controller.DTO.DescuentoDTO;
import com.unrn.model.Descuento;
import com.unrn.repository.DescuentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;

    public DescuentoService(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    public List<DescuentoDTO> obtenerTodos() {
        return descuentoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DescuentoDTO> obtenerDescuentosActivos() {
        LocalDateTime ahora = LocalDateTime.now();
        return descuentoRepository.findByActivoTrueAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(ahora, ahora)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DescuentoDTO crearDescuento(DescuentoDTO dto) {
        Descuento descuento = new Descuento();
        mapToEntity(dto, descuento);
        return mapToDTO(descuentoRepository.save(descuento));
    }

    public DescuentoDTO actualizarDescuento(Long id, DescuentoDTO dto) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        
        mapToEntity(dto, descuento);
        return mapToDTO(descuentoRepository.save(descuento));
    }

    public void eliminarDescuento(Long id) {
        descuentoRepository.deleteById(id);
    }

    private DescuentoDTO mapToDTO(Descuento descuento) {
        DescuentoDTO dto = new DescuentoDTO();
        dto.setId(descuento.getId());
        dto.setTitulo(descuento.getTitulo());
        dto.setMontoDescuento(descuento.getMontoDescuento());
        dto.setFechaInicio(descuento.getFechaInicio());
        dto.setFechaFin(descuento.getFechaFin());
        dto.setActivo(descuento.getActivo());
        return dto;
    }

    private void mapToEntity(DescuentoDTO dto, Descuento descuento) {
        descuento.setTitulo(dto.getTitulo());
        descuento.setMontoDescuento(dto.getMontoDescuento());
        descuento.setFechaInicio(dto.getFechaInicio());
        descuento.setFechaFin(dto.getFechaFin());
        if (dto.getActivo() != null) {
            descuento.setActivo(dto.getActivo());
        }
    }
}

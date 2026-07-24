package com.unrn.descuentos.controller;

import com.unrn.descuentos.dto.DescuentoDTO;
import com.unrn.descuentos.service.DescuentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentos")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @PostMapping
    public DescuentoDTO crear(@RequestBody DescuentoDTO dto) {
        return descuentoService.crearDescuento(dto);
    }

    @PutMapping("/{id}")
    public DescuentoDTO editar(
            @PathVariable Integer id,
            @RequestBody DescuentoDTO dto) {
        return descuentoService.editarDescuento(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        descuentoService.eliminarDescuento(id);
    }

    @GetMapping("/{id}")
    public DescuentoDTO obtenerDetalle(@PathVariable Integer id) {
        return descuentoService.obtenerDescuentoPorId(id);
    }

    @GetMapping
    public List<DescuentoDTO> listarActivos() {
        return descuentoService.listarDescuentosActivos();
    }

    @GetMapping("/todos")
    public List<DescuentoDTO> listarTodos() {
        return descuentoService.listarTodosLosDescuentos();
    }
}

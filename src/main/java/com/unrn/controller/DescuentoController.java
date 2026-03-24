package com.unrn.controller;

import com.unrn.controller.DTO.DescuentoDTO;
import com.unrn.services.DescuentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping
    public ResponseEntity<List<DescuentoDTO>> listarTodos() {
        return ResponseEntity.ok(descuentoService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<DescuentoDTO>> listarActivos() {
        return ResponseEntity.ok(descuentoService.obtenerDescuentosActivos());
    }

    @PostMapping
    public ResponseEntity<DescuentoDTO> crear(@RequestBody DescuentoDTO descuentoDTO) {
        DescuentoDTO creado = descuentoService.crearDescuento(descuentoDTO);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoDTO> actualizar(@PathVariable Long id, @RequestBody DescuentoDTO descuentoDTO) {
        return ResponseEntity.ok(descuentoService.actualizarDescuento(id, descuentoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        descuentoService.eliminarDescuento(id);
        return ResponseEntity.noContent().build();
    }
}

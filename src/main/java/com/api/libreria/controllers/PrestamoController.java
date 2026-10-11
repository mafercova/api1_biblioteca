package com.api.libreria.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoService.obtenerPrestamos();
    }

    @GetMapping("/activos")
    public List<PrestamoModel> obtenerPrestamosActivos() {
        return prestamoService.obtenerPrestamosActivos();
    }

    @PostMapping("/{id}/devolver")
    public PrestamoModel devolverPrestamo(@PathVariable Long id) {
        return prestamoService.devolverPrestamo(id);
    }

    @PutMapping("/{id}")
    public PrestamoModel actualizarPrestamo(@PathVariable Long id, @RequestParam Long isbn,
            @RequestParam Long personaId) {
        return prestamoService.actualizarPrestamo(id, isbn, personaId);
    }

    @DeleteMapping("/{id}")
    public void eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
    }
}
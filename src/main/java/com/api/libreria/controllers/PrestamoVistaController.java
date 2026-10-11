package com.api.libreria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.libreria.exceptions.ConflictException;
import com.api.libreria.exceptions.NotFoundException;
import com.api.libreria.services.LibroService;
import com.api.libreria.services.PersonaService;
import com.api.libreria.services.PrestamoService;

@Controller
public class PrestamoVistaController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final PersonaService personaService;

    public PrestamoVistaController(PrestamoService prestamoService, LibroService libroService,
            PersonaService personaService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.personaService = personaService;
    }

    @org.springframework.web.bind.annotation.GetMapping("/vista/prestamos")
    public String mostrarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.obtenerPrestamos());
        model.addAttribute("libros", libroService.obtenerLibros());
        model.addAttribute("personas", personaService.obtenerPersonas());
        return "prestamos";
    }

    @PostMapping("/vista/prestamos/guardar")
    public String guardarPrestamo(@RequestParam Long isbn, @RequestParam Long personaId,
            RedirectAttributes atributos) {
        try {
            prestamoService.registrarPrestamo(isbn, personaId);
            atributos.addFlashAttribute("mensaje", "Préstamo registrado correctamente.");
        } catch (IllegalArgumentException | NotFoundException | ConflictException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/devolver")
    public String devolverPrestamo(@org.springframework.web.bind.annotation.PathVariable Long id,
            RedirectAttributes atributos) {
        try {
            prestamoService.devolverPrestamo(id);
            atributos.addFlashAttribute("mensaje", "Libro devuelto correctamente.");
        } catch (IllegalArgumentException | NotFoundException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/actualizar")
    public String actualizarPrestamo(@PathVariable Long id, @RequestParam Long isbn,
            @RequestParam Long personaId, RedirectAttributes atributos) {
        try {
            prestamoService.actualizarPrestamo(id, isbn, personaId);
            atributos.addFlashAttribute("mensaje", "Préstamo actualizado correctamente.");
        } catch (IllegalArgumentException | NotFoundException | ConflictException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }

    @PostMapping("/vista/prestamos/{id}/eliminar")
    public String eliminarPrestamo(@PathVariable Long id, RedirectAttributes atributos) {
        try {
            prestamoService.eliminarPrestamo(id);
            atributos.addFlashAttribute("mensaje", "Préstamo eliminado correctamente.");
        } catch (IllegalArgumentException | NotFoundException exception) {
            atributos.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/vista/prestamos";
    }
}
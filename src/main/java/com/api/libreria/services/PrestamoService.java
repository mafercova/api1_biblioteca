package com.api.libreria.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.libreria.models.LibroModel;
import com.api.libreria.models.PersonaModel;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.repositories.ILibroRepository;
import com.api.libreria.repositories.IPersonaRepository;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class PrestamoService {

    private final IPrestamoRepository prestamoRepository;
    private final ILibroRepository libroRepository;
    private final IPersonaRepository personaRepository;

    public PrestamoService(IPrestamoRepository prestamoRepository, ILibroRepository libroRepository,
            IPersonaRepository personaRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.personaRepository = personaRepository;
    }

    public List<PrestamoModel> obtenerPrestamos() {
        return prestamoRepository.findAll();
    }

    public List<PrestamoModel> obtenerPrestamosActivos() {
        return prestamoRepository.findByFechaDevolucionIsNull();
    }

    @Transactional
    public PrestamoModel registrarPrestamo(Long isbn, Long personaId) {
        if (isbn == null || personaId == null) {
            throw new IllegalArgumentException("Debe seleccionar un libro y un usuario.");
        }
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe el libro seleccionado."));
        PersonaModel persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario seleccionado."));
        if (!"Activo".equalsIgnoreCase(persona.getEstado())) {
            throw new IllegalArgumentException("El usuario está inactivo.");
        }
        if (!"Usuario".equalsIgnoreCase(persona.getRol()) && !"Admin".equalsIgnoreCase(persona.getRol())) {
            throw new IllegalArgumentException("El rol del usuario no es válido.");
        }
        if (prestamoRepository.findBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(personaId, isbn).isPresent()) {
            throw new IllegalArgumentException("El usuario ya tiene este libro prestado.");
        }
        if (libro.getNumeroEjemplares() < 0) {
            throw new IllegalArgumentException("El número de ejemplares no puede ser negativo.");
        }
        if (libro.getNumeroEjemplares() <= 0) {
            throw new IllegalArgumentException("El libro no tiene ejemplares disponibles.");
        }
        libro.setNumeroEjemplares(libro.getNumeroEjemplares() - 1);
        libroRepository.save(libro);

        PrestamoModel prestamo = new PrestamoModel();
        prestamo.setLibro(libro);
        prestamo.setSocio(persona);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public PrestamoModel devolverPrestamo(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe indicar el préstamo que desea devolver.");
        }
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo seleccionado."));
        if (prestamo.getFechaDevolucion() == null) {
            prestamo.setFechaDevolucion(LocalDateTime.now());
            LibroModel libro = prestamo.getLibro();
            libro.setNumeroEjemplares(libro.getNumeroEjemplares() + 1);
            libroRepository.save(libro);
        }
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public PrestamoModel actualizarPrestamo(Long id, Long isbn, Long personaId) {
        if (id == null || isbn == null || personaId == null) {
            throw new IllegalArgumentException("Debe indicar el préstamo, libro y usuario.");
        }
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo seleccionado."));
        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalArgumentException("No se puede editar un préstamo devuelto.");
        }

        LibroModel nuevoLibro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe el libro seleccionado."));
        PersonaModel nuevaPersona = personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario seleccionado."));
        validarUsuario(nuevaPersona);

        if (prestamoRepository.findBySocioIdAndLibroIsbnAndFechaDevolucionIsNull(personaId, isbn)
                .filter(otroPrestamo -> !otroPrestamo.getId().equals(id)).isPresent()) {
            throw new IllegalArgumentException("El usuario ya tiene este libro prestado.");
        }

        LibroModel libroAnterior = prestamo.getLibro();
        if (!libroAnterior.getIsbn().equals(nuevoLibro.getIsbn())) {
            if (nuevoLibro.getNumeroEjemplares() <= 0) {
                throw new IllegalArgumentException("El nuevo libro no tiene ejemplares disponibles.");
            }
            libroAnterior.setNumeroEjemplares(libroAnterior.getNumeroEjemplares() + 1);
            nuevoLibro.setNumeroEjemplares(nuevoLibro.getNumeroEjemplares() - 1);
            libroRepository.save(libroAnterior);
            libroRepository.save(nuevoLibro);
        }
        prestamo.setLibro(nuevoLibro);
        prestamo.setSocio(nuevaPersona);
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public void eliminarPrestamo(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe indicar el préstamo que desea eliminar.");
        }
        PrestamoModel prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo seleccionado."));
        if (prestamo.getFechaDevolucion() == null) {
            LibroModel libro = prestamo.getLibro();
            libro.setNumeroEjemplares(libro.getNumeroEjemplares() + 1);
            libroRepository.save(libro);
        }
        prestamoRepository.delete(prestamo);
    }

    private void validarUsuario(PersonaModel persona) {
        if (!"Activo".equalsIgnoreCase(persona.getEstado())) {
            throw new IllegalArgumentException("El usuario está inactivo.");
        }
        if (!"Usuario".equalsIgnoreCase(persona.getRol()) && !"Admin".equalsIgnoreCase(persona.getRol())) {
            throw new IllegalArgumentException("El rol del usuario no es válido.");
        }
    }
}
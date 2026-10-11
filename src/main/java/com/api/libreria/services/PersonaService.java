package com.api.libreria.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.exceptions.NotFoundException;
import com.api.libreria.models.PersonaModel;
import com.api.libreria.repositories.IPersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private IPersonaRepository personaRepository;

    public List<PersonaModel> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public PersonaModel guardarPersona(PersonaModel persona) {
        validarPersona(persona, true);
        return personaRepository.save(persona);
    }

    public ArrayList<PersonaModel> buscarPorNombre(String nombre) {
        return personaRepository.findByNombre(nombre);
    }

    public Optional<PersonaModel> obtenerPorId(Long id) {
        return personaRepository.findById(id);
    }

    public PersonaModel actualizarPersona(PersonaModel request, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe indicar el usuario que desea actualizar.");
        }
        validarPersona(request, false);
        PersonaModel persona = personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe una persona con ID: " + id));
        persona.setNombre(request.getNombre());
        persona.setApellidos(request.getApellidos());
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            persona.setContrasena(request.getContrasena());
        }
        persona.setTelefono(request.getTelefono());
        persona.setCorreoElectronico(request.getCorreoElectronico());
        persona.setDireccion(request.getDireccion());
        persona.setSexo(request.getSexo());
        persona.setFechaNacimiento(request.getFechaNacimiento());
        persona.setEstado(request.getEstado());
        persona.setRol(request.getRol());
        return personaRepository.save(persona);
    }

    private void validarPersona(PersonaModel persona, boolean requiereContrasena) {
        if (persona == null || persona.getNombre() == null || persona.getNombre().isBlank()
                || persona.getApellidos() == null || persona.getApellidos().isBlank()) {
            throw new IllegalArgumentException("El nombre y los apellidos son obligatorios.");
        }
        if (requiereContrasena && (persona.getContrasena() == null || persona.getContrasena().isBlank())) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (persona.getTelefono() == null || persona.getTelefono() <= 0) {
            throw new IllegalArgumentException("El teléfono debe ser válido.");
        }
        if (persona.getCorreoElectronico() == null || persona.getCorreoElectronico().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (!"Activo".equalsIgnoreCase(persona.getEstado()) && !"Inactivo".equalsIgnoreCase(persona.getEstado())) {
            throw new IllegalArgumentException("El estado debe ser Activo o Inactivo.");
        }
        if (!"Usuario".equalsIgnoreCase(persona.getRol()) && !"Admin".equalsIgnoreCase(persona.getRol())) {
            throw new IllegalArgumentException("El rol debe ser Usuario o Admin.");
        }
    }

    public boolean eliminarPersona(Long id) {
        try {
            eliminarPersonaPorId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void eliminarPersonaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe indicar la persona que desea eliminar.");
        }
        if (!personaRepository.existsById(id)) {
            throw new NotFoundException("No existe una persona con ID: " + id);
        }
        personaRepository.deleteById(id);
    }
}

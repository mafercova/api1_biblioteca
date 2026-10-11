package com.api.libreria.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.PersonaModel;
import com.api.libreria.services.PersonaService;

@RestController
@RequestMapping("/personas")
public class PersonaController {

	@Autowired
	private PersonaService personaService;

	@GetMapping
	public List<PersonaModel> obtenerPersonas() {
		return personaService.obtenerPersonas();
	}

	@PostMapping
	public PersonaModel guardarPersona(@RequestBody PersonaModel persona) {
		return personaService.guardarPersona(persona);
	}

	@GetMapping("/{id}")
	public Optional<PersonaModel> obtenerPorId(@PathVariable("id") Long id) {
		return personaService.obtenerPorId(id);
	}

	@PutMapping("/{id}")
	public PersonaModel actualizarPersona(@RequestBody PersonaModel request, @PathVariable("id") Long id) {
		return personaService.actualizarPersona(request, id);
	}

	@DeleteMapping("/{id}")
	public String eliminarPorId(@PathVariable("id") Long id) {
		boolean ok = personaService.eliminarPersona(id);
		if (ok) {
			return "Se eliminó la persona con ID: " + id;
		} else {
			return "No pudo eliminar la persona con ID: " + id;
		}
	}

	@GetMapping("/nombre/{nombre}")
	public ArrayList<PersonaModel> buscarPorNombre(@PathVariable("nombre") String nombre) {
		return personaService.buscarPorNombre(nombre);
	}
}

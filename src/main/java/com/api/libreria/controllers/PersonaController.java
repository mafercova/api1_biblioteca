package com.api.libreria.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.exceptions.NotFoundException;
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
	public PersonaModel obtenerPorId(@PathVariable("id") Long id) {
		return personaService.obtenerPorId(id)
				.orElseThrow(() -> new NotFoundException("No existe una persona con ID: " + id));
	}

	@PutMapping("/{id}")
	public PersonaModel actualizarPersona(@RequestBody PersonaModel request, @PathVariable("id") Long id) {
		return personaService.actualizarPersona(request, id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarPorId(@PathVariable("id") Long id) {
		personaService.eliminarPersonaPorId(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/nombre/{nombre}")
	public ArrayList<PersonaModel> buscarPorNombre(@PathVariable("nombre") String nombre) {
		return personaService.buscarPorNombre(nombre);
	}
}

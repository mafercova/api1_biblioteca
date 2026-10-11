package com.api.libreria.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;

@Service
public class LibroService {
    
    @Autowired
    ILibroRepository libroRepository;

    public ArrayList<LibroModel> obtenerLibros() {
        return (ArrayList<LibroModel>) libroRepository.findAll();
    }

    public LibroModel guardarLibro(LibroModel libro) {
        validarLibro(libro);
        return libroRepository.save(libro);
    }

    public ArrayList<LibroModel> buscarPorAutor(String autor) {
        return libroRepository.findByAutor(autor);
    }

    public Optional<LibroModel> obtenerPorId(Long isbn) {
        return libroRepository.findById(isbn);
    }

    public LibroModel actualizarLibro(LibroModel request, Long isbn) {
        validarLibro(request);
        LibroModel libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No existe un libro con ISBN: " + isbn));
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAñoPublicacion(request.getAñoPublicacion());
        libro.setNumeroEjemplares(request.getNumeroEjemplares());
        return libroRepository.save(libro);
    }

    private void validarLibro(LibroModel libro) {
        if (libro == null || libro.getTitulo() == null || libro.getTitulo().isBlank()
                || libro.getAutor() == null || libro.getAutor().isBlank()) {
            throw new IllegalArgumentException("El título y el autor son obligatorios.");
        }
        if (libro.getNumeroEjemplares() < 0) {
            throw new IllegalArgumentException("El número de ejemplares no puede ser negativo.");
        }
    }

    public boolean eliminarLibro(Long isbn){
        try{
            libroRepository.deleteById(isbn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
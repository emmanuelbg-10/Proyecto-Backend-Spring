package com.emmanuel.biblioteca.service;

import com.emmanuel.biblioteca.entity.Autor;
import com.emmanuel.biblioteca.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    AutorRepository autorRepository;

    public List<Autor> getAutores(){
        return autorRepository.findAll();
    }

    public Optional<Autor> getAutorById(Integer id){
        return autorRepository.findById(id);
    }

    public Autor saveOrUpdate(Autor autor){
        autorRepository.save(autor);
        return autor;
    }

    public void deleteAutorById(Integer autorId) {
        Optional<Autor> autor = autorRepository.findById(autorId);
        if (autor.isPresent()) {
            autorRepository.deleteById(autorId);
        } else {
            throw new RuntimeException("El autor con ID " + autorId + " no existe.");
        }
    }
}
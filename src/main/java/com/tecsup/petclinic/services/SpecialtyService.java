package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;

import java.util.List;

public interface SpecialtyService {

    Specialty create(Specialty specialty);

    Specialty update(Specialty specialty);

    void delete(Long id) throws SpecialtyNotFoundException;

    Specialty findById(Long id) throws SpecialtyNotFoundException;

    List<Specialty> findByName(String name);

    List<Specialty> findAll();
}
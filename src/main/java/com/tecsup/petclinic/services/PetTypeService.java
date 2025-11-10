
package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;

import java.util.List;

public interface PetTypeService {

    PetType create(PetType petType);

    PetType update(PetType petType);

    void delete(Long id) throws PetTypeNotFoundException;

    PetType findById(Long id) throws PetTypeNotFoundException;

    List<PetType> findByName(String name);

    List<PetType> findAll();
}
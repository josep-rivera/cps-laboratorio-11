package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialty create(VetSpecialty vetSpecialty);

    VetSpecialty update(VetSpecialty vetSpecialty);

    void delete(Long vetId, Long specialtyId) throws VetSpecialtyNotFoundException;

    VetSpecialty findById(Long vetId, Long specialtyId) throws VetSpecialtyNotFoundException;

    List<VetSpecialty> findByVetId(Long vetId);

    List<VetSpecialty> findBySpecialtyId(Long specialtyId);

    List<VetSpecialty> findAll();
}
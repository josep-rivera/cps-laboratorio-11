
package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    @Autowired
    private VetSpecialtyRepository vetSpecialtyRepository;

    @Override
    public VetSpecialty create(VetSpecialty vetSpecialty) {
        return vetSpecialtyRepository.save(vetSpecialty);
    }

    @Override
    public VetSpecialty update(VetSpecialty vetSpecialty) {
        return vetSpecialtyRepository.save(vetSpecialty);
    }

    @Override
    public void delete(Long vetId, Long specialtyId) throws VetSpecialtyNotFoundException {
        VetSpecialty vetSpecialty = findById(vetId, specialtyId);
        vetSpecialtyRepository.delete(vetSpecialty);
    }

    @Override
    public VetSpecialty findById(Long vetId, Long specialtyId) throws VetSpecialtyNotFoundException {
        VetSpecialtyId id = new VetSpecialtyId(vetId, specialtyId);
        Optional<VetSpecialty> vetSpecialty = vetSpecialtyRepository.findById(id);
        if (!vetSpecialty.isPresent())
            throw new VetSpecialtyNotFoundException("Relación Vet-Specialty no encontrada: vetId=" + vetId + ", specialtyId=" + specialtyId);
        return vetSpecialty.get();
    }

    @Override
    public List<VetSpecialty> findByVetId(Long vetId) {
        return vetSpecialtyRepository.findByVetId(vetId);
    }

    @Override
    public List<VetSpecialty> findBySpecialtyId(Long specialtyId) {
        return vetSpecialtyRepository.findBySpecialtyId(specialtyId);
    }

    @Override
    public List<VetSpecialty> findAll() {
        return vetSpecialtyRepository.findAll();
    }
}
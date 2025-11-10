
package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VetServiceImpl implements VetService {

    @Autowired
    private VetRepository vetRepository;

    @Override
    public Vet create(Vet vet) {
        return vetRepository.save(vet);
    }

    @Override
    public Vet update(Vet vet) {
        return vetRepository.save(vet);
    }

    @Override
    public void delete(Long id) throws VetNotFoundException {
        Vet vet = findById(id);
        vetRepository.delete(vet);
    }

    @Override
    public Vet findById(Long id) throws VetNotFoundException {
        Optional<Vet> vet = vetRepository.findById(id);
        if (!vet.isPresent())
            throw new VetNotFoundException("Veterinario no encontrado con ID: " + id);
        return vet.get();
    }

    @Override
    public List<Vet> findByFirstName(String firstName) {
        return vetRepository.findByFirstName(firstName);
    }

    @Override
    public List<Vet> findByLastName(String lastName) {
        return vetRepository.findByLastName(lastName);
    }

    @Override
    public List<Vet> findAll() {
        return vetRepository.findAll();
    }
}
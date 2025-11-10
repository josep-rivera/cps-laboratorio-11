
package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
@Slf4j
public class PetTypeController {

    @Autowired
    private PetTypeService petTypeService;

    @GetMapping
    public ResponseEntity<List<PetType>> findAll() {
        List<PetType> petTypes = petTypeService.findAll();
        return ResponseEntity.ok(petTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetType> findById(@PathVariable Long id) {
        try {
            PetType petType = petTypeService.findById(id);
            return ResponseEntity.ok(petType);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PetType> create(@RequestBody PetType petType) {
        PetType newPetType = petTypeService.create(petType);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPetType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetType> update(@PathVariable Long id, @RequestBody PetType petType) {
        try {
            PetType existingPetType = petTypeService.findById(id);
            existingPetType.setName(petType.getName());
            PetType updatedPetType = petTypeService.update(existingPetType);
            return ResponseEntity.ok(updatedPetType);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            petTypeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
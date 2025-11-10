package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@Slf4j
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        List<Specialty> specialties = specialtyService.findAll();
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Long id) {
        try {
            Specialty specialty = specialtyService.findById(id);
            return ResponseEntity.ok(specialty);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody Specialty specialty) {
        Specialty newSpecialty = specialtyService.create(specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable Long id, @RequestBody Specialty specialty) {
        try {
            Specialty existingSpecialty = specialtyService.findById(id);
            existingSpecialty.setName(specialty.getName());
            Specialty updatedSpecialty = specialtyService.update(existingSpecialty);
            return ResponseEntity.ok(updatedSpecialty);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
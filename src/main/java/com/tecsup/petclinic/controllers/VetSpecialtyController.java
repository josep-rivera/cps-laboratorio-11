package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.services.VetSpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vet-specialties")
@Slf4j
public class VetSpecialtyController {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @GetMapping
    public ResponseEntity<List<VetSpecialty>> findAll() {
        List<VetSpecialty> vetSpecialties = vetSpecialtyService.findAll();
        return ResponseEntity.ok(vetSpecialties);
    }

    @GetMapping("/{vetId}/{specialtyId}")
    public ResponseEntity<VetSpecialty> findById(@PathVariable Long vetId, @PathVariable Long specialtyId) {
        try {
            VetSpecialty vetSpecialty = vetSpecialtyService.findById(vetId, specialtyId);
            return ResponseEntity.ok(vetSpecialty);
        } catch (VetSpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vet/{vetId}")
    public ResponseEntity<List<VetSpecialty>> findByVetId(@PathVariable Long vetId) {
        List<VetSpecialty> vetSpecialties = vetSpecialtyService.findByVetId(vetId);
        return ResponseEntity.ok(vetSpecialties);
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<VetSpecialty>> findBySpecialtyId(@PathVariable Long specialtyId) {
        List<VetSpecialty> vetSpecialties = vetSpecialtyService.findBySpecialtyId(specialtyId);
        return ResponseEntity.ok(vetSpecialties);
    }

    @PostMapping
    public ResponseEntity<VetSpecialty> create(@RequestBody VetSpecialty vetSpecialty) {
        VetSpecialty newVetSpecialty = vetSpecialtyService.create(vetSpecialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVetSpecialty);
    }

    @PutMapping("/{vetId}/{specialtyId}")
    public ResponseEntity<VetSpecialty> update(@PathVariable Long vetId, @PathVariable Long specialtyId,
                                               @RequestBody VetSpecialty vetSpecialty) {
        try {
            VetSpecialty existingVetSpecialty = vetSpecialtyService.findById(vetId, specialtyId);
            // Eliminar la relación anterior
            vetSpecialtyService.delete(vetId, specialtyId);
            // Crear la nueva relación
            VetSpecialty updatedVetSpecialty = vetSpecialtyService.create(vetSpecialty);
            return ResponseEntity.ok(updatedVetSpecialty);
        } catch (VetSpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{vetId}/{specialtyId}")
    public ResponseEntity<Void> delete(@PathVariable Long vetId, @PathVariable Long specialtyId) {
        try {
            vetSpecialtyService.delete(vetId, specialtyId);
            return ResponseEntity.noContent().build();
        } catch (VetSpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
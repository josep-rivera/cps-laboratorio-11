package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
@Slf4j
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public ResponseEntity<List<Owner>> findAll() {
        List<Owner> owners = ownerService.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable Long id) {
        try {
            Owner owner = ownerService.findById(id);
            return ResponseEntity.ok(owner);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        Owner newOwner = ownerService.create(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        try {
            Owner existingOwner = ownerService.findById(id);
            existingOwner.setFirstName(owner.getFirstName());
            existingOwner.setLastName(owner.getLastName());
            existingOwner.setAddress(owner.getAddress());
            existingOwner.setCity(owner.getCity());
            existingOwner.setTelephone(owner.getTelephone());
            Owner updatedOwner = ownerService.update(existingOwner);
            return ResponseEntity.ok(updatedOwner);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            ownerService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
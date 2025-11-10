
package com.tecsup.petclinic.controllers;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@Slf4j
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping
    public ResponseEntity<List<Visit>> findAll() {
        List<Visit> visits = visitService.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> findById(@PathVariable Integer id) {
        try {
            Visit visit = visitService.findById(id);
            return ResponseEntity.ok(visit);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Visit> create(@RequestBody Visit visit) {
        Visit newVisit = visitService.create(visit);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> update(@PathVariable Integer id, @RequestBody Visit visit) {
        try {
            Visit existingVisit = visitService.findById(id);
            existingVisit.setVisitDate(visit.getVisitDate());
            existingVisit.setDescription(visit.getDescription());
            existingVisit.setPetId(visit.getPetId());
            Visit updatedVisit = visitService.update(existingVisit);
            return ResponseEntity.ok(updatedVisit);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            visitService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
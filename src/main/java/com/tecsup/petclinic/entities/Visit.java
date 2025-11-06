package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pet_id", nullable = false)
    private Integer petId;

    @Column(name = "vet_id")
    private Integer vetId;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Double cost;

    // Constructor sin ID para crear nuevas visitas
    public Visit(LocalDate visitDate, String description, Integer petId) {
        this.visitDate = visitDate;
        this.description = description;
        this.petId = petId;
    }

    // Constructor completo sin ID
    public Visit(Integer petId, Integer vetId, LocalDate visitDate, String description, Double cost) {
        this.petId = petId;
        this.vetId = vetId;
        this.visitDate = visitDate;
        this.description = description;
        this.cost = cost;
    }
}
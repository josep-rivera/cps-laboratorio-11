package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vet_specialties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VetSpecialtyId.class)
public class VetSpecialty {

    @Id
    @Column(name = "vet_id")
    private Long vetId;

    @Id
    @Column(name = "specialty_id")
    private Long specialtyId;
}
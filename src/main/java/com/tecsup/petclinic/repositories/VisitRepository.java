package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    // Buscar visitas por petId
    List<Visit> findByPetId(Integer petId);

    // Buscar visitas por vetId
    List<Visit> findByVetId(Integer vetId);

    // Buscar visitas por rango de fechas
    List<Visit> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    // Buscar visitas por fecha específica
    List<Visit> findByVisitDate(LocalDate visitDate);

    // Buscar visitas por pet y fecha
    List<Visit> findByPetIdAndVisitDate(Integer petId, LocalDate visitDate);
}
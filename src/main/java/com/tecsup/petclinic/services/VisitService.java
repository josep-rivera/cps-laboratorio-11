package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;

import java.time.LocalDate;
import java.util.List;

public interface VisitService {

    Visit create(Visit visit);

    Visit update(Visit visit);

    void delete(Integer id) throws Exception;

    Visit findById(Integer id) throws Exception;

    List<Visit> findAll();

    List<Visit> findByPetId(Integer petId);

    List<Visit> findByVetId(Integer vetId);

    List<Visit> findByDateRange(LocalDate startDate, LocalDate endDate);

    List<Visit> findByDate(LocalDate date);

    List<Visit> findByPetIdAndDate(Integer petId, LocalDate date);
}
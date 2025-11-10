package com.tecsup.petclinic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitService visitService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todas las visitas
     */
    @Test
    public void testGetAllVisits() throws Exception {
        log.info("✅ Ejecutando test: obtener todas las visitas");

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear una nueva visita
     */
    @Test
    public void testCreateVisit() throws Exception {
        log.info("✅ Ejecutando test: crear una nueva visita");

        Visit newVisit = new Visit(LocalDate.of(2024, 12, 1), "Control de vacunación", 1);
        String visitJson = objectMapper.writeValueAsString(newVisit);

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("Control de vacunación")))
                .andExpect(jsonPath("$.petId", is(1)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * Test para obtener una visita por ID
     */
    @Test
    public void testGetVisitById() throws Exception {
        log.info("✅ Ejecutando test: obtener visita por ID");

        Visit visit = visitService.create(new Visit(LocalDate.of(2024, 11, 15), "Revisión general", 2));

        mockMvc.perform(get("/visits/" + visit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(visit.getId().intValue())))
                .andExpect(jsonPath("$.description", is("Revisión general")))
                .andExpect(jsonPath("$.petId", is(2)));
    }

    /**
     * Test para actualizar una visita
     */
    @Test
    public void testUpdateVisit() throws Exception {
        log.info("✅ Ejecutando test: actualizar visita");

        Visit visit = visitService.create(new Visit(LocalDate.of(2024, 10, 20), "Consulta inicial", 3));

        visit.setDescription("Consulta de seguimiento");
        String visitJson = objectMapper.writeValueAsString(visit);

        mockMvc.perform(put("/visits/" + visit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Consulta de seguimiento")));
    }

    /**
     * Test para eliminar una visita
     */
    @Test
    public void testDeleteVisit() throws Exception {
        log.info("✅ Ejecutando test: eliminar visita");

        Visit visit = visitService.create(new Visit(LocalDate.of(2024, 9, 10), "Examen de rutina", 4));

        mockMvc.perform(delete("/visits/" + visit.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/visits/" + visit.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar visita no encontrada
     */
    @Test
    public void testGetVisitNotFound() throws Exception {
        log.info("✅ Ejecutando test: visita no encontrada");

        mockMvc.perform(get("/visits/99999"))
                .andExpect(status().isNotFound());
    }
}
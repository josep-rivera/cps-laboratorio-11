package com.tecsup.petclinic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todas las especialidades
     */
    @Test
    public void testGetAllSpecialties() throws Exception {
        log.info("✅ Ejecutando test: obtener todas las especialidades");

        mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear una nueva especialidad
     */
    @Test
    public void testCreateSpecialty() throws Exception {
        log.info("✅ Ejecutando test: crear una nueva especialidad");

        Specialty newSpecialty = new Specialty("Cirugía");
        String specialtyJson = objectMapper.writeValueAsString(newSpecialty);

        mockMvc.perform(post("/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(specialtyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Cirugía")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * Test para obtener una especialidad por ID
     */
    @Test
    public void testGetSpecialtyById() throws Exception {
        log.info("✅ Ejecutando test: obtener especialidad por ID");

        Specialty specialty = specialtyService.create(new Specialty("Radiología"));

        mockMvc.perform(get("/specialties/" + specialty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(specialty.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Radiología")));
    }

    /**
     * Test para actualizar una especialidad
     */
    @Test
    public void testUpdateSpecialty() throws Exception {
        log.info("✅ Ejecutando test: actualizar especialidad");

        Specialty specialty = specialtyService.create(new Specialty("Dermatología"));

        specialty.setName("Dermatología Avanzada");
        String specialtyJson = objectMapper.writeValueAsString(specialty);

        mockMvc.perform(put("/specialties/" + specialty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(specialtyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dermatología Avanzada")));
    }

    /**
     * Test para eliminar una especialidad
     */
    @Test
    public void testDeleteSpecialty() throws Exception {
        log.info("✅ Ejecutando test: eliminar especialidad");

        Specialty specialty = specialtyService.create(new Specialty("Odontología"));

        mockMvc.perform(delete("/specialties/" + specialty.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/specialties/" + specialty.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar especialidad no encontrada
     */
    @Test
    public void testGetSpecialtyNotFound() throws Exception {
        log.info("✅ Ejecutando test: especialidad no encontrada");

        mockMvc.perform(get("/specialties/99999"))
                .andExpect(status().isNotFound());
    }
}
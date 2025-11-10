package com.tecsup.petclinic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.services.SpecialtyService;
import com.tecsup.petclinic.services.VetService;
import com.tecsup.petclinic.services.VetSpecialtyService;
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
public class VetSpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetService vetService;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todas las relaciones vet-specialty
     */
    @Test
    public void testGetAllVetSpecialties() throws Exception {
        log.info("✅ Ejecutando test: obtener todas las relaciones vet-specialty");

        mockMvc.perform(get("/vet-specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear una nueva relación vet-specialty
     */
    @Test
    public void testCreateVetSpecialty() throws Exception {
        log.info("✅ Ejecutando test: crear una nueva relación vet-specialty");

        // Primero crear el veterinario y la especialidad
        Vet vet = vetService.create(new Vet("Dr. Carlos", "Méndez"));
        Specialty specialty = specialtyService.create(new Specialty("Cardiología"));

        VetSpecialty newVetSpecialty = new VetSpecialty(vet.getId(), specialty.getId());
        String vetSpecialtyJson = objectMapper.writeValueAsString(newVetSpecialty);

        mockMvc.perform(post("/vet-specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vetSpecialtyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vetId", is(vet.getId().intValue())))
                .andExpect(jsonPath("$.specialtyId", is(specialty.getId().intValue())));
    }

    /**
     * Test para obtener una relación vet-specialty por ID compuesto
     */
    @Test
    public void testGetVetSpecialtyById() throws Exception {
        log.info("✅ Ejecutando test: obtener relación vet-specialty por ID compuesto");

        // Crear veterinario y especialidad
        Vet vet = vetService.create(new Vet("Dra. Ana", "López"));
        Specialty specialty = specialtyService.create(new Specialty("Neurología"));

        VetSpecialty vetSpecialty = vetSpecialtyService.create(new VetSpecialty(vet.getId(), specialty.getId()));

        mockMvc.perform(get("/vet-specialties/" + vetSpecialty.getVetId() + "/" + vetSpecialty.getSpecialtyId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vetId", is(vet.getId().intValue())))
                .andExpect(jsonPath("$.specialtyId", is(specialty.getId().intValue())));
    }

    /**
     * Test para obtener especialidades por veterinario
     */
    @Test
    public void testGetByVetId() throws Exception {
        log.info("✅ Ejecutando test: obtener especialidades por veterinario");

        // Crear veterinario y especialidades
        Vet vet = vetService.create(new Vet("Dr. Pedro", "Ramírez"));
        Specialty specialty1 = specialtyService.create(new Specialty("Pediatría"));
        Specialty specialty2 = specialtyService.create(new Specialty("Geriatría"));

        vetSpecialtyService.create(new VetSpecialty(vet.getId(), specialty1.getId()));
        vetSpecialtyService.create(new VetSpecialty(vet.getId(), specialty2.getId()));

        mockMvc.perform(get("/vet-specialties/vet/" + vet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$[*].vetId", everyItem(is(vet.getId().intValue()))));
    }

    /**
     * Test para obtener veterinarios por especialidad
     */
    @Test
    public void testGetBySpecialtyId() throws Exception {
        log.info("✅ Ejecutando test: obtener veterinarios por especialidad");

        // Crear veterinarios y especialidad
        Vet vet1 = vetService.create(new Vet("Dr. Luis", "Fernández"));
        Vet vet2 = vetService.create(new Vet("Dra. María", "Torres"));
        Specialty specialty = specialtyService.create(new Specialty("Oftalmología"));

        vetSpecialtyService.create(new VetSpecialty(vet1.getId(), specialty.getId()));
        vetSpecialtyService.create(new VetSpecialty(vet2.getId(), specialty.getId()));

        mockMvc.perform(get("/vet-specialties/specialty/" + specialty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$[*].specialtyId", everyItem(is(specialty.getId().intValue()))));
    }

    /**
     * Test para actualizar una relación vet-specialty
     */
    @Test
    public void testUpdateVetSpecialty() throws Exception {
        log.info("✅ Ejecutando test: actualizar relación vet-specialty");

        // Crear veterinario y especialidades
        Vet vet = vetService.create(new Vet("Dr. Jorge", "Martínez"));
        Specialty specialty1 = specialtyService.create(new Specialty("Traumatología"));
        Specialty specialty2 = specialtyService.create(new Specialty("Rehabilitación"));

        VetSpecialty vetSpecialty = vetSpecialtyService.create(new VetSpecialty(vet.getId(), specialty1.getId()));

        VetSpecialty updatedData = new VetSpecialty(vet.getId(), specialty2.getId());
        String vetSpecialtyJson = objectMapper.writeValueAsString(updatedData);

        mockMvc.perform(put("/vet-specialties/" + vetSpecialty.getVetId() + "/" + vetSpecialty.getSpecialtyId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vetSpecialtyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialtyId", is(specialty2.getId().intValue())));
    }

    /**
     * Test para eliminar una relación vet-specialty
     */
    @Test
    public void testDeleteVetSpecialty() throws Exception {
        log.info("✅ Ejecutando test: eliminar relación vet-specialty");

        // Crear veterinario y especialidad
        Vet vet = vetService.create(new Vet("Dra. Carmen", "Ruiz"));
        Specialty specialty = specialtyService.create(new Specialty("Endocrinología"));

        VetSpecialty vetSpecialty = vetSpecialtyService.create(new VetSpecialty(vet.getId(), specialty.getId()));

        mockMvc.perform(delete("/vet-specialties/" + vetSpecialty.getVetId() + "/" + vetSpecialty.getSpecialtyId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/vet-specialties/" + vetSpecialty.getVetId() + "/" + vetSpecialty.getSpecialtyId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar relación vet-specialty no encontrada
     */
    @Test
    public void testGetVetSpecialtyNotFound() throws Exception {
        log.info("✅ Ejecutando test: relación vet-specialty no encontrada");

        mockMvc.perform(get("/vet-specialties/99999/99999"))
                .andExpect(status().isNotFound());
    }
}
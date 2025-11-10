package com.tecsup.petclinic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.services.VetService;
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
public class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VetService vetService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todos los veterinarios
     */
    @Test
    public void testGetAllVets() throws Exception {
        log.info("✅ Ejecutando test: obtener todos los veterinarios");

        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear un nuevo veterinario
     */
    @Test
    public void testCreateVet() throws Exception {
        log.info("✅ Ejecutando test: crear un nuevo veterinario");

        Vet newVet = new Vet("Carlos", "Rodríguez");
        String vetJson = objectMapper.writeValueAsString(newVet);

        mockMvc.perform(post("/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vetJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("Rodríguez")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * Test para obtener un veterinario por ID
     */
    @Test
    public void testGetVetById() throws Exception {
        log.info("✅ Ejecutando test: obtener veterinario por ID");

        Vet vet = vetService.create(new Vet("Ana", "Martínez"));

        mockMvc.perform(get("/vets/" + vet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(vet.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("Ana")))
                .andExpect(jsonPath("$.lastName", is("Martínez")));
    }

    /**
     * Test para actualizar un veterinario
     */
    @Test
    public void testUpdateVet() throws Exception {
        log.info("✅ Ejecutando test: actualizar veterinario");

        Vet vet = vetService.create(new Vet("Pedro", "López"));

        vet.setFirstName("Pedro Actualizado");
        vet.setLastName("López Actualizado");
        String vetJson = objectMapper.writeValueAsString(vet);

        mockMvc.perform(put("/vets/" + vet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vetJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Pedro Actualizado")))
                .andExpect(jsonPath("$.lastName", is("López Actualizado")));
    }

    /**
     * Test para eliminar un veterinario
     */
    @Test
    public void testDeleteVet() throws Exception {
        log.info("✅ Ejecutando test: eliminar veterinario");

        Vet vet = vetService.create(new Vet("María", "González"));

        mockMvc.perform(delete("/vets/" + vet.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/vets/" + vet.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar veterinario no encontrado
     */
    @Test
    public void testGetVetNotFound() throws Exception {
        log.info("✅ Ejecutando test: veterinario no encontrado");

        mockMvc.perform(get("/vets/99999"))
                .andExpect(status().isNotFound());
    }
}
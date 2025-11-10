package com.tecsup.petclinic.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService;
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
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todos los propietarios
     */
    @Test
    public void testGetAllOwners() throws Exception {
        log.info("✅ Ejecutando test: obtener todos los propietarios");

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear un nuevo propietario
     */
    @Test
    public void testCreateOwner() throws Exception {
        log.info("✅ Ejecutando test: crear un nuevo propietario");

        Owner newOwner = new Owner("Juan", "Pérez", "Av. Larco 123", "Lima", "987654321");
        String ownerJson = objectMapper.writeValueAsString(newOwner);

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("Pérez")))
                .andExpect(jsonPath("$.city", is("Lima")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * Test para obtener un propietario por ID
     */
    @Test
    public void testGetOwnerById() throws Exception {
        log.info("✅ Ejecutando test: obtener propietario por ID");

        Owner owner = ownerService.create(new Owner("María", "García", "Jr. Puno 456", "Arequipa", "912345678"));

        mockMvc.perform(get("/owners/" + owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(owner.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("María")))
                .andExpect(jsonPath("$.lastName", is("García")))
                .andExpect(jsonPath("$.city", is("Arequipa")));
    }

    /**
     * Test para actualizar un propietario
     */
    @Test
    public void testUpdateOwner() throws Exception {
        log.info("✅ Ejecutando test: actualizar propietario");

        Owner owner = ownerService.create(new Owner("Luis", "Torres", "Calle Lima 789", "Cusco", "923456789"));

        owner.setFirstName("Luis Actualizado");
        owner.setCity("Lima");
        String ownerJson = objectMapper.writeValueAsString(owner);

        mockMvc.perform(put("/owners/" + owner.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ownerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Luis Actualizado")))
                .andExpect(jsonPath("$.city", is("Lima")));
    }

    /**
     * Test para eliminar un propietario
     */
    @Test
    public void testDeleteOwner() throws Exception {
        log.info("✅ Ejecutando test: eliminar propietario");

        Owner owner = ownerService.create(new Owner("Carmen", "Ruiz", "Av. Brasil 321", "Trujillo", "934567890"));

        mockMvc.perform(delete("/owners/" + owner.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/owners/" + owner.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar propietario no encontrado
     */
    @Test
    public void testGetOwnerNotFound() throws Exception {
        log.info("✅ Ejecutando test: propietario no encontrado");

        mockMvc.perform(get("/owners/99999"))
                .andExpect(status().isNotFound());
    }
}
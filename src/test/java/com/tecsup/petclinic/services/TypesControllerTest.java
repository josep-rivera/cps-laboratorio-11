package com.tecsup.petclinic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.services.PetTypeService;
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
public class TypesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetTypeService petTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test para obtener todos los tipos de mascotas
     */
    @Test
    public void testGetAllTypes() throws Exception {
        log.info("✅ Ejecutando test: obtener todos los tipos de mascotas");

        mockMvc.perform(get("/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    /**
     * Test para crear un nuevo tipo de mascota
     */
    @Test
    public void testCreateType() throws Exception {
        log.info("✅ Ejecutando test: crear un nuevo tipo de mascota");

        PetType newType = new PetType("Perro");
        String typeJson = objectMapper.writeValueAsString(newType);

        mockMvc.perform(post("/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Perro")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    /**
     * Test para obtener un tipo de mascota por ID
     */
    @Test
    public void testGetTypeById() throws Exception {
        log.info("✅ Ejecutando test: obtener tipo de mascota por ID");

        PetType petType = petTypeService.create(new PetType("Gato"));

        mockMvc.perform(get("/types/" + petType.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(petType.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Gato")));
    }

    /**
     * Test para actualizar un tipo de mascota
     */
    @Test
    public void testUpdateType() throws Exception {
        log.info("✅ Ejecutando test: actualizar tipo de mascota");

        PetType petType = petTypeService.create(new PetType("Ave"));

        petType.setName("Ave Exótica");
        String typeJson = objectMapper.writeValueAsString(petType);

        mockMvc.perform(put("/types/" + petType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ave Exótica")));
    }

    /**
     * Test para eliminar un tipo de mascota
     */
    @Test
    public void testDeleteType() throws Exception {
        log.info("✅ Ejecutando test: eliminar tipo de mascota");

        PetType petType = petTypeService.create(new PetType("Reptil"));

        mockMvc.perform(delete("/types/" + petType.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/types/" + petType.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para manejar tipo de mascota no encontrado
     */
    @Test
    public void testGetTypeNotFound() throws Exception {
        log.info("✅ Ejecutando test: tipo de mascota no encontrado");

        mockMvc.perform(get("/types/99999"))
                .andExpect(status().isNotFound());
    }
}
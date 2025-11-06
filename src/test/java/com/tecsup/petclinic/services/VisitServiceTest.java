package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;

    // ==================== PRUEBAS DE CREACIÓN ====================

    /**
     * Prueba la creación básica de una visita con datos mínimos requeridos.
     * Verifica que se asigne un ID y los datos se guarden correctamente.
     */
    @Test
    public void testCreateVisit() {
        LocalDate visitDate = LocalDate.of(2024, 11, 6);
        String description = "Checkup regular";
        Integer petId = 1;

        Visit visit = new Visit(visitDate, description, petId);
        visit = visitService.create(visit);

        log.info("✅ Visit created: " + visit.toString());

        assertNotNull(visit.getId());
        assertEquals(visitDate, visit.getVisitDate());
        assertEquals(description, visit.getDescription());
        assertEquals(petId, visit.getPetId());
    }

    /**
     * Prueba la creación de una visita completa con todos los campos opcionales.
     * Verifica que se guarden correctamente el veterinario y el costo.
     */
    @Test
    public void testCreateVisit_Complete() {
        LocalDate visitDate = LocalDate.of(2024, 11, 7);
        String description = "Surgery consultation";
        Integer petId = 2;
        Integer vetId = 3;
        Double cost = 150.00;

        Visit visit = new Visit(petId, vetId, visitDate, description, cost);
        visit = visitService.create(visit);

        log.info("✅ Complete visit created: " + visit.toString());

        assertNotNull(visit.getId());
        assertEquals(visitDate, visit.getVisitDate());
        assertEquals(description, visit.getDescription());
        assertEquals(petId, visit.getPetId());
        assertEquals(vetId, visit.getVetId());
        assertEquals(cost, visit.getCost());
    }

    /**
     * Prueba que el sistema permita crear visitas sin descripción.
     * Verifica que los campos nulos sean aceptados correctamente.
     */
    @Test
    public void testCreateVisit_WithNullDescription() {
        LocalDate visitDate = LocalDate.of(2024, 11, 6);
        String description = null;
        Integer petId = 1;

        Visit visit = new Visit(visitDate, description, petId);
        Visit createdVisit = visitService.create(visit);

        log.info("✅ Visit created with null description: " + createdVisit);

        assertNotNull(createdVisit);
        assertNotNull(createdVisit.getId());
        assertNull(createdVisit.getDescription());
    }

    /**
     * Prueba la creación de visitas con fechas futuras (citas programadas).
     * Verifica que el sistema acepte fechas posteriores a hoy.
     */
    @Test
    public void testCreateVisit_WithFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        String description = "Cita programada";
        Integer petId = 1;

        Visit visit = new Visit(futureDate, description, petId);
        visit = visitService.create(visit);

        log.info("✅ Future visit created: " + visit.toString());

        assertNotNull(visit.getId());
        assertEquals(futureDate, visit.getVisitDate());
        assertTrue(visit.getVisitDate().isAfter(LocalDate.now()));
    }

    /**
     * Prueba la creación de visitas con fechas pasadas (historial médico).
     * Verifica que el sistema acepte registros históricos.
     */
    @Test
    public void testCreateVisit_WithPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(30);
        String description = "Visita histórica";
        Integer petId = 1;

        Visit visit = new Visit(pastDate, description, petId);
        visit = visitService.create(visit);

        log.info("✅ Past visit created: " + visit.toString());

        assertNotNull(visit.getId());
        assertEquals(pastDate, visit.getVisitDate());
        assertTrue(visit.getVisitDate().isBefore(LocalDate.now()));
    }

    // ==================== PRUEBAS DE BÚSQUEDA ====================

    /**
     * Prueba la búsqueda de una visita por su ID.
     * Verifica que se recupere correctamente una visita existente.
     */
    @Test
    public void testFindVisitById() {
        Integer visitId = 1;

        try {
            Visit visit = visitService.findById(visitId);
            log.info("✅ Visit found: " + visit.toString());

            assertNotNull(visit);
            assertEquals(visitId, visit.getId());
            assertNotNull(visit.getVisitDate());
        } catch (Exception e) {
            fail("Visit not found: " + e.getMessage());
        }
    }

    /**
     * Prueba el manejo de errores al buscar una visita inexistente.
     * Verifica que se lance una excepción con mensaje apropiado.
     */
    @Test
    public void testFindVisitByIdNotFound() {
        Integer visitId = 99999;

        Exception exception = assertThrows(Exception.class, () -> {
            visitService.findById(visitId);
        });

        log.info("✅ Expected exception: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("not found"));
    }

    /**
     * Prueba la recuperación de todas las visitas registradas.
     * Verifica que la lista no esté vacía y contenga datos válidos.
     */
    @Test
    public void testFindAllVisits() {
        List<Visit> visits = visitService.findAll();

        log.info("✅ Total visits found: " + visits.size());

        assertNotNull(visits);
        assertTrue(visits.size() > 0);

        visits.forEach(visit -> {
            assertNotNull(visit.getId());
            assertNotNull(visit.getVisitDate());
        });
    }

    /**
     * Prueba la búsqueda de visitas por mascota (pet_id).
     * Verifica que se recuperen solo las visitas de la mascota especificada.
     */
    @Test
    public void testFindVisitsByPetId() {
        Integer petId = 1;

        List<Visit> visits = visitService.findByPetId(petId);

        log.info("✅ Visits found for pet " + petId + ": " + visits.size());

        assertNotNull(visits);

        visits.forEach(visit -> {
            assertEquals(petId, visit.getPetId());
            log.info("  - Visit: " + visit.getDescription() + " on " + visit.getVisitDate());
        });
    }

    /**
     * Prueba la búsqueda de visitas para una mascota inexistente.
     * Verifica que se devuelva una lista vacía sin errores.
     */
    @Test
    public void testFindVisitsByPetIdNotFound() {
        Integer petId = 99999;

        List<Visit> visits = visitService.findByPetId(petId);

        log.info("✅ Visits found for non-existent pet: " + visits.size());

        assertNotNull(visits);
        assertTrue(visits.isEmpty());
    }

    /**
     * Prueba la búsqueda de visitas por veterinario (vet_id).
     * Verifica que se recuperen solo las visitas atendidas por ese veterinario.
     */
    @Test
    public void testFindVisitsByVetId() {
        Integer vetId = 2;

        List<Visit> visits = visitService.findByVetId(vetId);

        log.info("✅ Visits found for vet " + vetId + ": " + visits.size());

        assertNotNull(visits);

        visits.forEach(visit -> {
            assertEquals(vetId, visit.getVetId());
        });
    }

    /**
     * Prueba la búsqueda de visitas dentro de un rango de fechas.
     * Verifica que solo se recuperen visitas dentro del período especificado.
     */
    @Test
    public void testFindVisitsByDateRange() {
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.now();

        List<Visit> visits = visitService.findByDateRange(startDate, endDate);

        log.info("✅ Visits found in date range: " + visits.size());

        assertNotNull(visits);
        assertTrue(visits.size() > 0);

        visits.forEach(visit -> {
            assertTrue(visit.getVisitDate().isAfter(startDate) ||
                    visit.getVisitDate().isEqual(startDate));
            assertTrue(visit.getVisitDate().isBefore(endDate) ||
                    visit.getVisitDate().isEqual(endDate));
        });
    }

    /**
     * Prueba la búsqueda de visitas en una fecha específica.
     * Verifica que se recuperen solo visitas de ese día exacto.
     */
    @Test
    public void testFindVisitsByDate() {
        LocalDate specificDate = LocalDate.of(2010, 3, 4);

        List<Visit> visits = visitService.findByDate(specificDate);

        log.info("✅ Visits found on " + specificDate + ": " + visits.size());

        assertNotNull(visits);

        visits.forEach(visit -> {
            assertEquals(specificDate, visit.getVisitDate());
        });
    }

    /**
     * Prueba la búsqueda de visitas por mascota y fecha específica.
     * Verifica que se filtren correctamente por ambos criterios.
     */
    @Test
    public void testFindVisitsByPetIdAndDate() {
        Integer petId = 7;
        LocalDate visitDate = LocalDate.of(2010, 3, 4);

        List<Visit> visits = visitService.findByPetIdAndDate(petId, visitDate);

        log.info("✅ Visits found for pet " + petId + " on " + visitDate + ": " + visits.size());

        assertNotNull(visits);

        visits.forEach(visit -> {
            assertEquals(petId, visit.getPetId());
            assertEquals(visitDate, visit.getVisitDate());
        });
    }

    // ==================== PRUEBAS DE ACTUALIZACIÓN ====================

    /**
     * Prueba la actualización de la descripción y costo de una visita existente.
     * Verifica que los cambios se persistan correctamente en la base de datos.
     */
    @Test
    public void testUpdateVisit() {
        Integer visitId = 1;

        try {
            Visit visit = visitService.findById(visitId);
            String originalDescription = visit.getDescription();
            String newDescription = "Actualización - Vacunación completa";
            Double newCost = 85.00;

            visit.setDescription(newDescription);
            visit.setCost(newCost);

            Visit updatedVisit = visitService.update(visit);

            log.info("✅ Visit updated:");
            log.info("  - Original: " + originalDescription);
            log.info("  - Updated: " + updatedVisit.getDescription());
            log.info("  - New cost: " + updatedVisit.getCost());

            assertNotNull(updatedVisit);
            assertEquals(visitId, updatedVisit.getId());
            assertEquals(newDescription, updatedVisit.getDescription());
            assertEquals(newCost, updatedVisit.getCost());
        } catch (Exception e) {
            fail("Update failed: " + e.getMessage());
        }
    }

    /**
     * Prueba la actualización de la fecha de una visita.
     * Verifica que se pueda reprogramar una visita correctamente.
     */
    @Test
    public void testUpdateVisit_ChangeDate() {
        Integer visitId = 2;

        try {
            Visit visit = visitService.findById(visitId);
            LocalDate originalDate = visit.getVisitDate();
            LocalDate newDate = LocalDate.now();

            visit.setVisitDate(newDate);

            Visit updatedVisit = visitService.update(visit);

            log.info("✅ Visit date updated:");
            log.info("  - Original: " + originalDate);
            log.info("  - Updated: " + updatedVisit.getVisitDate());

            assertNotNull(updatedVisit);
            assertEquals(newDate, updatedVisit.getVisitDate());
            assertNotEquals(originalDate, updatedVisit.getVisitDate());
        } catch (Exception e) {
            fail("Update failed: " + e.getMessage());
        }
    }

    /**
     * Prueba la asignación de veterinario y costo a una visita sin estos datos.
     * Verifica que se puedan agregar campos opcionales después de la creación.
     */
    @Test
    public void testUpdateVisit_AddVetAndCost() {
        Visit visit = new Visit(LocalDate.now(), "Checkup sin vet", 1);
        visit = visitService.create(visit);

        log.info("✅ Visit created without vet: " + visit);

        visit.setVetId(2);
        visit.setCost(75.00);

        Visit updatedVisit = visitService.update(visit);

        log.info("✅ Visit updated with vet and cost: " + updatedVisit);

        assertEquals(2, updatedVisit.getVetId());
        assertEquals(75.00, updatedVisit.getCost());
    }

    /**
     * Prueba el manejo de errores al intentar actualizar una visita inexistente.
     * Verifica que se lance una excepción apropiada.
     */
    @Test
    public void testUpdateVisit_NotFound() {
        Visit visit = new Visit();
        visit.setId(99999); 
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Test update non-existent");
        visit.setPetId(1);

        assertThrows(Exception.class, () -> {
            visitService.update(visit);
        });

        log.info("✅ Expected exception when updating non-existent visit");
    }

    // ==================== PRUEBAS DE ELIMINACIÓN ====================

    /**
     * Prueba la eliminación exitosa de una visita.
     * Verifica que la visita sea removida de la base de datos completamente.
     */
    @Test
    public void testDeleteVisit() {
        LocalDate visitDate = LocalDate.of(2024, 12, 1);
        String description = "Visita temporal para prueba de eliminación";
        Integer petId = 1;

        Visit visit = new Visit(visitDate, description, petId);
        visit = visitService.create(visit);
        Integer visitId = visit.getId();

        log.info("✅ Visit created for deletion: ID=" + visitId);

        try {
            visitService.delete(visitId);
            log.info("✅ Visit deleted successfully: ID=" + visitId);

            Exception exception = assertThrows(Exception.class, () -> {
                visitService.findById(visitId);
            });

            assertTrue(exception.getMessage().contains("not found"));

        } catch (Exception e) {
            fail("Delete failed: " + e.getMessage());
        }
    }

    /**
     * Prueba el manejo de errores al intentar eliminar una visita inexistente.
     * Verifica que se lance una excepción con mensaje descriptivo.
     */
    @Test
    public void testDeleteVisit_NotFound() {
        Integer visitId = 99999;

        Exception exception = assertThrows(Exception.class, () -> {
            visitService.delete(visitId);
        });

        log.info("✅ Expected exception when deleting non-existent visit: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("not found"));
    }

    /**
     * Prueba la integridad del conteo de visitas después de crear y eliminar.
     * Verifica que el número total de registros sea consistente.
     */
    @Test
    public void testDeleteVisit_VerifyCount() {
        long countBefore = visitRepository.count();
        log.info("✅ Visits before deletion: " + countBefore);

        Visit visit = new Visit(LocalDate.now(), "Temporal", 1);
        visit = visitService.create(visit);
        Integer visitId = visit.getId();

        long countAfterCreate = visitRepository.count();
        log.info("✅ Visits after creation: " + countAfterCreate);
        assertEquals(countBefore + 1, countAfterCreate);

        try {
            visitService.delete(visitId);

            long countAfterDelete = visitRepository.count();
            log.info("✅ Visits after deletion: " + countAfterDelete);
            assertEquals(countBefore, countAfterDelete);

        } catch (Exception e) {
            fail("Delete failed: " + e.getMessage());
        }
    }

    // ==================== PRUEBAS DE VALIDACIÓN ADICIONALES ====================

    /**
     * Prueba la creación de múltiples visitas para la misma mascota.
     * Verifica que se puedan registrar varios eventos médicos sin conflictos.
     */
    @Test
    public void testCreateMultipleVisitsForSamePet() {
        Integer petId = 5;

        Visit visit1 = new Visit(LocalDate.now(), "Primera visita", petId);
        Visit visit2 = new Visit(LocalDate.now().plusDays(7), "Segunda visita", petId);
        Visit visit3 = new Visit(LocalDate.now().plusDays(14), "Tercera visita", petId);

        visit1 = visitService.create(visit1);
        visit2 = visitService.create(visit2);
        visit3 = visitService.create(visit3);

        log.info("✅ Created 3 visits for pet " + petId);

        List<Visit> petVisits = visitService.findByPetId(petId);

        assertTrue(petVisits.size() >= 3);
        log.info("✅ Total visits for pet " + petId + ": " + petVisits.size());
    }

    /**
     * Prueba el cálculo y actualización del costo de una visita.
     * Verifica que los valores monetarios se manejen correctamente.
     */
    @Test
    public void testVisitCostCalculation() {
        Visit visit = new Visit(LocalDate.now(), "Consultation", 1);
        visit.setCost(50.00);
        visit = visitService.create(visit);

        log.info("✅ Visit created with cost: $" + visit.getCost());

        assertEquals(50.00, visit.getCost());

        visit.setCost(75.50);
        visit = visitService.update(visit);

        log.info("✅ Visit cost updated to: $" + visit.getCost());

        assertEquals(75.50, visit.getCost());
    }
}
package ru.itis.impulse_back.junit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.SpecialtyController;
import ru.itis.impulse_back.dto.request.EditSpecialtiesRequest;
import ru.itis.impulse_back.dto.response.SpecialtiesResponse;
import ru.itis.impulse_back.mapper.SpecialtyMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.service.SpecialtyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpecialtyControllerTest {

    private SpecialtyService specialtyService;
    private SpecialtyMapper specialtyMapper;
    private SpecialtyController specialtyController;

    @BeforeEach
    void setUp() {
        specialtyService = mock(SpecialtyService.class);
        specialtyMapper = mock(SpecialtyMapper.class);
        specialtyController = new SpecialtyController(specialtyService, specialtyMapper);
    }

    @Test
    void testGetAll_returnsListOfSpecialties() {
        List<Specialty> specialties = List.of(
                new Specialty(1L, "Fitness",false),
                new Specialty(2L, "Pilates",false)
        );
        when(specialtyService.getAll()).thenReturn(specialties);

        ResponseEntity<List<Specialty>> response = specialtyController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Fitness", response.getBody().get(0).getName());
    }

    @Test
    void testEditSpecialties_addsNewSpecialtyAndReturnsMappedList() {
        // given
        String newSpecialtyName = "Yoga";
        EditSpecialtiesRequest request = new EditSpecialtiesRequest();
        request.setNewSpecialty(newSpecialtyName);

        List<Specialty> updatedSpecialties = List.of(
                new Specialty(1L, "Fitness",false),
                new Specialty(2L, "Yoga",false)
        );

        List<SpecialtiesResponse> mappedResponses = List.of(
                new SpecialtiesResponse(1L, "Fitness",false),
                new SpecialtiesResponse(2L, "Yoga",false)
        );

        when(specialtyService.getAll()).thenReturn(updatedSpecialties);
        when(specialtyMapper.toResponseList(updatedSpecialties)).thenReturn(mappedResponses);

        // when
        ResponseEntity<List<SpecialtiesResponse>> response = specialtyController.editSpecialties(request);

        // then
        verify(specialtyService).addSpecialty(newSpecialtyName, true);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Yoga", response.getBody().get(1).getName());
    }
}

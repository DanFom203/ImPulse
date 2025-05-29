package ru.itis.impulse_back.junit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.itis.impulse_back.controller.SpecialtyController;
import ru.itis.impulse_back.dto.request.EditSpecialtiesRequest;
import ru.itis.impulse_back.dto.response.SpecialtiesResponse;
import ru.itis.impulse_back.mapper.SpecialtyMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.service.SpecialtyService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpecialtyControllerTest {

    @Mock
    private SpecialtyService specialtyService;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyController specialtyController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(specialtyController).build();
    }

    @Test
    void getAll_ShouldReturnListOfSpecialties() throws Exception {
        List<Specialty> specialties = List.of(
                Specialty.builder().id(1L).name("Specialty 1").build(),
                Specialty.builder().id(2L).name("Specialty 2").build()
        );

        when(specialtyService.getAll()).thenReturn(specialties);

        mockMvc.perform(get("/api/specialty/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Specialty 1")))
                .andExpect(jsonPath("$[1].name", is("Specialty 2")));

        verify(specialtyService, times(1)).getAll();
    }

    @Test
    void editSpecialties_ShouldAddNewSpecialtyAndReturnUpdatedList() throws Exception {
        EditSpecialtiesRequest request = new EditSpecialtiesRequest();
        request.setNewSpecialty("New Specialty");

        List<Specialty> specialties = List.of(
                Specialty.builder().id(1L).name("Old Specialty").build(),
                Specialty.builder().id(2L).name("New Specialty").build()
        );

        List<SpecialtiesResponse> responseList = List.of(
                SpecialtiesResponse.builder().id(1L).name("Old Specialty").build(),
                SpecialtiesResponse.builder().id(2L).name("New Specialty").build()
        );

        when(specialtyService.getAll()).thenReturn(specialties);
        when(specialtyMapper.toResponseList(specialties)).thenReturn(responseList);

        mockMvc.perform(post("/api/specialty/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("New Specialty")));

        verify(specialtyService, times(1)).addSpecialty("New Specialty", true);
        verify(specialtyService, times(1)).getAll();
        verify(specialtyMapper, times(1)).toResponseList(specialties);
    }
}

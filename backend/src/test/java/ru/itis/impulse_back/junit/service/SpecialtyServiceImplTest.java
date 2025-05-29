package ru.itis.impulse_back.junit.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.repository.SpecialtyRepository;
import ru.itis.impulse_back.service.impl.SpecialtyServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceImplTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @InjectMocks
    private SpecialtyServiceImpl specialtyService;

    @Test
    void getAll_ShouldReturnAllSpecialties() {
        // Arrange
        Specialty s1 = new Specialty();
        Specialty s2 = new Specialty();
        List<Specialty> specialties = Arrays.asList(s1, s2);

        when(specialtyRepository.findAll()).thenReturn(specialties);

        // Act
        List<Specialty> result = specialtyService.getAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(specialtyRepository, times(1)).findAll();
    }

    @Test
    void addSpecialty_ShouldCallSaveCustomSpecialty() {
        // Arrange
        String newSpecialty = "Yoga";
        boolean isCustom = true;

        // Act
        specialtyService.addSpecialty(newSpecialty, isCustom);

        // Assert
        verify(specialtyRepository, times(1)).saveCustomSpecialty(newSpecialty, isCustom);
    }
}

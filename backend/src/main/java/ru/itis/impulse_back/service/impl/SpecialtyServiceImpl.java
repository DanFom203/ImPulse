package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.repository.SpecialtyRepository;
import ru.itis.impulse_back.service.SpecialtyService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Override
    public List<Specialty> getAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public void addSpecialty(String newSpecialty, boolean isCustom) {
        specialtyRepository.saveCustomSpecialty(newSpecialty, isCustom);
    }
}

package ru.itis.impulse_back.service;

import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.model.User;

import java.util.List;

public interface SpecialistService {
    List<User> getAllByFilter(SearchSpecialistRequest request);
    User getById(Long id);
    void editSpecialties(List<Long> specialties, Long specialistId);

    void addSpecialty(String newSpecialty, boolean isCustom);
}

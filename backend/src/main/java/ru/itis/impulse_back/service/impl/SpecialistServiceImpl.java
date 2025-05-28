package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.exception.SpecialistNotFoundException;
import ru.itis.impulse_back.exception.SpecialtyNotFoundException;
import ru.itis.impulse_back.mapper.UserDetailsMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.SpecialistRepository;
import ru.itis.impulse_back.repository.SpecialtyRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.SpecialistService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialistServiceImpl implements SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public List<User> getAllByFilter(SearchSpecialistRequest request) {
        return specialistRepository.findSpecialistsByFilter(request);
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty() || !user.get().getRole().equals(User.UserRole.SPECIALIST)) {
            throw new SpecialistNotFoundException("Specialist with id " + id + " not found");
        }

        return user.get();
    }

    @Override
    public void updateSpecialties(List<Long> specialties, Long specialistId) {
        List<Specialty> newSpecialties = new ArrayList<>();

        for (Long specialtyId : specialties) {
            Optional<Specialty> specialtyOptional = specialtyRepository.findById(specialtyId);

            if (specialtyOptional.isEmpty()) {
                throw new SpecialtyNotFoundException("Specialty with id " + specialtyId + " not found");
            }

            newSpecialties.add(specialtyOptional.get());
        }

        User specialist = getById(specialistId);
        specialist.setSpecialties(newSpecialties);

        userRepository.save(specialist);
    }

    @Override
    public UserDetailsResponse updateSpecialistInfo(Long specialistId, String specialistBio, Integer price) {
        User specialist = getById(specialistId);

        if (specialistBio != null) specialist.setSpecialistBio(specialistBio);

        if (price != null) specialist.setSpecialistAppointmentPrice(price);

        userRepository.save(specialist);
        return userDetailsMapper.toResponse(specialist);
    }
}

package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.EditSpecialtiesRequest;
import ru.itis.impulse_back.dto.response.SpecialtiesResponse;
import ru.itis.impulse_back.mapper.SpecialtyMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.service.SpecialtyService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri}/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final SpecialtyMapper specialtyMapper;

    @GetMapping("/list")
    public ResponseEntity<List<Specialty>> getAll() {
        log.info("Fetching list of all specialties");
        List<Specialty> specialties = specialtyService.getAll();
        log.debug("Found {} specialties", specialties.size());
        return ResponseEntity.ok(specialties);
    }

    @PostMapping("/edit")
    public ResponseEntity<List<SpecialtiesResponse>> editSpecialties(@RequestBody EditSpecialtiesRequest request) {
        log.info("Editing specialties. New specialty: {}", request.getNewSpecialty());

        specialtyService.addSpecialty(request.getNewSpecialty(), true);

        List<SpecialtiesResponse> specialties = specialtyMapper.toResponseList(specialtyService.getAll());
        log.debug("Updated specialties list now has {} entries", specialties.size());

        return ResponseEntity.ok(specialties);
    }
}

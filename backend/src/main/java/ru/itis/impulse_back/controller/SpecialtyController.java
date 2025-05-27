package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.EditSpecialtiesRequest;
import ru.itis.impulse_back.dto.response.SpecialtiesResponse;
import ru.itis.impulse_back.mapper.SpecialtyMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.service.SpecialtyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri}/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final SpecialtyMapper specialtyMapper;

    @GetMapping("/list")
    public ResponseEntity<List<Specialty>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(specialtyService.getAll());
    }

    @PostMapping("/edit")
    public ResponseEntity<List<SpecialtiesResponse>> editSpecialties(@RequestBody EditSpecialtiesRequest request) {
        specialtyService.addSpecialty(request.getNewSpecialty(), true);
        List<SpecialtiesResponse> specialties = specialtyMapper.toResponseList(specialtyService.getAll());
        return ResponseEntity.ok(specialties);
    }
}

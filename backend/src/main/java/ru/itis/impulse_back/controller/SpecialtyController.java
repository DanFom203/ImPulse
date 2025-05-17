package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.service.SpecialtyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri}/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<Specialty>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(specialtyService.getAll());
    }
}

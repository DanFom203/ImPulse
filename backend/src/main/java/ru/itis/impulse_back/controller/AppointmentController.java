package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.CreateAppointmentRequest;
import ru.itis.impulse_back.dto.response.AppointmentResponse;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("${api.uri}/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final JWTService jwtService;

    @PostMapping("/new")
    public ResponseEntity<Void> create(@RequestBody CreateAppointmentRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        appointmentService.create(request, jwtService.getClaims(token.substring(7)).get("id").asLong());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/approve/{appointmentId}")
    public ResponseEntity<Void> approve(@PathVariable Long appointmentId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        appointmentService.approve(appointmentId, jwtService.getClaims(token.substring(7)).get("id").asLong());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponse>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<AppointmentResponse> appointments = appointmentService.getAllByUserId(jwtService.getClaims(token.substring(7)).get("id").asLong())
                .stream()
                .map(a -> AppointmentResponse.builder()
                        .id(a.getId())
                        .client(AppointmentResponse.ClientInfo.builder()
                                .id(a.getClient().getId())
                                .fullName(a.getClient().getFullName())
                                .build())
                        .specialist(AppointmentResponse.SpecialistInfo.builder()
                                .id(a.getSpecialist().getId())
                                .fullName(a.getSpecialist().getFullName())
                                .rating(a.getSpecialist().getSpecialistAvgRating())
                                .specialties(a.getSpecialist().getSpecialties())
                                .build())
                        .price(a.getPrice())
                        .createdAt(a.getCreatedAt())
                        .scheduledAt(a.getScheduledAt())
                        .isApproved(a.getIsApproved())
                        .build()).toList();

        return ResponseEntity.ok(appointments);
    }
}

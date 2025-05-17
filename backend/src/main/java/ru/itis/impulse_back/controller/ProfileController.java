package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.SpecialistService;
import ru.itis.impulse_back.service.UserService;
import java.util.List;

@RestController
@RequestMapping("${api.uri}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final JWTService jwtService;
    private final SpecialistService specialistService;
    private final UserService userService;

    @PostMapping("/edit/specialty")
    public ResponseEntity<Void> editSpecialties(@RequestBody List<Long> specialties, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        specialistService.editSpecialties(specialties, jwtService.getClaims(token.substring(7)).get("id").asLong());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/delete")
    public ResponseEntity<Void> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        userService.deleteAccount(jwtService.getClaims(token.substring(7)).get("id").asLong());
        return ResponseEntity.ok().build();
    }
}

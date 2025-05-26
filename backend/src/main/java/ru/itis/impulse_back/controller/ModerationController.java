package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.DeleteUserRequest;
import ru.itis.impulse_back.dto.request.UpdateAuthorityRequest;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.service.ModerationService;

import java.util.List;

@RestController
@RequestMapping("${api.uri}/moderation")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;


    @GetMapping("/accounts")
    public ResponseEntity<List<AccountModerationResponse>> getAllUserAccounts() {
        List<AccountModerationResponse> accounts = moderationService.getAllUsers();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/authority")
    public ResponseEntity<Void> updateAuthority(@RequestBody UpdateAuthorityRequest request) {
        moderationService.updateUserAuthorityByEmail(request.getEmail(), request.getAuthority());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @RequestBody DeleteUserRequest request) {
        System.out.println("TOKEN = " + token);
        moderationService.deleteUserByEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }
}

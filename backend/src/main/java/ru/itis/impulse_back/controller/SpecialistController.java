package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.dto.response.SearchSpecialistByIdResponse;
import ru.itis.impulse_back.dto.response.SearchSpecialistResponse;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.service.SpecialistService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri}/specialist")
public class SpecialistController {

    private final SpecialistService specialistService;

    @PostMapping("/find")
    public ResponseEntity<List<SearchSpecialistResponse>> searchSpecialists(@RequestBody SearchSpecialistRequest request) {
        List<User> specialists = specialistService.getAllByFilter(request);

        List<SearchSpecialistResponse> searchSpecialistResponse = new ArrayList<>();

        for (User specialist : specialists) {
            SearchSpecialistResponse response = SearchSpecialistResponse.builder()
                    .specialistId(specialist.getId())
                    .name(specialist.getFullName())
                    .rating(specialist.getSpecialistAvgRating())
                    .bio(specialist.getSpecialistBio())
                    .price(specialist.getSpecialistAppointmentPrice())
                    .specialties(specialist.getSpecialties())
                    .build();

            searchSpecialistResponse.add(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(searchSpecialistResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchSpecialistByIdResponse> getById(@PathVariable Long id) {
        User user = specialistService.getById(id);

        SearchSpecialistByIdResponse response = SearchSpecialistByIdResponse.builder()
                .fullName(user.getFullName())
                .price(user.getSpecialistAppointmentPrice())
                .rating(user.getSpecialistAvgRating())
                .bio(user.getSpecialistBio())
                .specialties(user.getSpecialties())
                .build();

        List<SearchSpecialistByIdResponse.ReviewInfo> reviews = user.getSpecialistReviews().stream()
                .map(r -> {
                    SearchSpecialistByIdResponse.ReviewInfo review = new SearchSpecialistByIdResponse.ReviewInfo();

                    review.setId(r.getId());
                    review.setClient(SearchSpecialistByIdResponse.ReviewInfo.ClientInfo.builder()
                            .id(r.getClient().getId())
                            .fullName(r.getClient().getFullName())
                            .build());
                    review.setRating(r.getRating());
                    review.setComment(r.getComment());
                    review.setCreatedAt(r.getCreatedAt());

                    return review;
                }).toList();

        response.setReviews(reviews);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

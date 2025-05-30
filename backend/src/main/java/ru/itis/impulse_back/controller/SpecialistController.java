package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri}/specialist")
public class SpecialistController {

    private final SpecialistService specialistService;

    @PostMapping("/find")
    public ResponseEntity<List<SearchSpecialistResponse>> searchSpecialists(@RequestBody SearchSpecialistRequest request) {
        log.info("Searching specialists with filter: {}", request);

        List<User> specialists = specialistService.getAllByFilter(request);
        log.debug("Found {} specialists matching filter", specialists.size());

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
        log.info("Fetching specialist details for ID: {}", id);
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

        log.debug("Returning specialist details for ID {} with {} reviews", id, reviews.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

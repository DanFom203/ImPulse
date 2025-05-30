package ru.itis.impulse_back.junit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.SpecialistController;
import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.dto.response.SearchSpecialistByIdResponse;
import ru.itis.impulse_back.dto.response.SearchSpecialistResponse;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.service.SpecialistService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpecialistsControllerTest {

    private SpecialistService specialistService;
    private SpecialistController specialistController;

    @BeforeEach
    void setUp() {
        specialistService = mock(SpecialistService.class);
        specialistController = new SpecialistController(specialistService);
    }

    @Test
    void searchSpecialists_shouldReturnListOfSearchSpecialistResponses() {
        SearchSpecialistRequest request = new SearchSpecialistRequest();

        User user = User.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .specialistAvgRating(4.5)
                .specialistBio("Опытный специалист")
                .specialistAppointmentPrice(3000)
                .build();

        when(specialistService.getAllByFilter(request)).thenReturn(List.of(user));

        ResponseEntity<List<SearchSpecialistResponse>> response = specialistController.searchSpecialists(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Иван Иванов", response.getBody().get(0).getName());
        assertEquals(3000, response.getBody().get(0).getPrice());
    }

    @Test
    void getById_shouldReturnSpecialistWithReviews() {
        User client = User.builder().id(2L).fullName("Клиент Тестов").build();
        Review review = Review.builder()
                .id(10L)
                .client(client)
                .rating(5)
                .comment("Отлично")
                .build();

        User specialist = User.builder()
                .id(1L)
                .fullName("Специалист Иван")
                .specialistAvgRating(4.7)
                .specialistBio("Био")
                .specialistAppointmentPrice(2500)
                .specialistReviews(List.of(review))
                .build();

        when(specialistService.getById(1L)).thenReturn(specialist);

        ResponseEntity<SearchSpecialistByIdResponse> response = specialistController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        SearchSpecialistByIdResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Специалист Иван", body.getFullName());
        assertEquals(2500, body.getPrice());
        assertEquals(1, body.getReviews().size());
        assertEquals("Клиент Тестов", body.getReviews().get(0).getClient().getFullName());
    }
}

package ru.itis.impulse_back.dto.response;

import lombok.*;
import ru.itis.impulse_back.model.Specialty;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private ClientInfo client;
    private SpecialistInfo specialist;
    private Integer rating;
    private String comment;
    private Date createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClientInfo {
        private Long id;
        private String fullName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpecialistInfo {
        private Long id;
        private String fullName;
        private Double rating;
        private List<Specialty> specialties;
    }
}

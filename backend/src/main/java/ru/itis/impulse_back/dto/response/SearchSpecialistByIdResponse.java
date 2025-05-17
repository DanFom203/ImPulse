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
public class SearchSpecialistByIdResponse {
    private String fullName;
    private Integer price;
    private Double rating;
    private String bio;
    private List<ReviewInfo> reviews;
    private List<Specialty> specialties;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewInfo {
        private Long id;
        private ClientInfo client;
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
    }
}

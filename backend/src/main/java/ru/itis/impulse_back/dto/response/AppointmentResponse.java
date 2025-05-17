package ru.itis.impulse_back.dto.response;

import lombok.*;
import java.util.Date;
import ru.itis.impulse_back.model.Specialty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private ClientInfo client;
    private SpecialistInfo specialist;
    private Integer price;
    private Date createdAt;
    private Date scheduledAt;
    private Boolean isApproved;

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

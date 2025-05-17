package ru.itis.impulse_back.dto;

import lombok.*;
import java.util.Date;
import java.util.List;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;

    private String fullName;

    private Date createdAt;

    private User.UserRole role;

    private String specialistBio;

    private Integer specialistAppointmentPrice;

    private Double specialistAvgRating;

    private List<Review> clientReviews;

    private List<Review> specialistReviews;

    private List<Specialty> specialties;
}

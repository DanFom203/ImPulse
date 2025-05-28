package ru.itis.impulse_back.dto;

import lombok.*;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.model.User;
import java.util.Date;
import java.util.List;

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

    private User.UserAuthority authority;

    private String specialistBio;

    private Integer specialistAppointmentPrice;

    private Double specialistAvgRating;

    private String profileImageUrl;

    private List<Review> clientReviews;

    private List<Review> specialistReviews;

    private List<Specialty> specialties;
}

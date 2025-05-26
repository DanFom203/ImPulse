package ru.itis.impulse_back.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModerationResponse {

    private String email;

    private String fullName;

    private String role;

    private String authority;

    private Date createdAt;

    private String profileImageUrl;

    @JsonProperty("rating")
    private Double specialistRating;

}

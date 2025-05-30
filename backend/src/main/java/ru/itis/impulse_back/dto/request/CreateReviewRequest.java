package ru.itis.impulse_back.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private String comment;
    private Integer rating;
}

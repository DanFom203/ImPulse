package ru.itis.impulse_back.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialtiesResponse {
    private Long id;
    private String name;
    private boolean isCustom;
}

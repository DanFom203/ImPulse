package ru.itis.impulse_back.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.itis.impulse_back.model.Specialty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchSpecialistResponse {

    @JsonProperty("id")
    private Long specialistId;

    private String name;

    private Double rating;

    private String bio;

    private Integer price;

    private List<Specialty> specialties;
}

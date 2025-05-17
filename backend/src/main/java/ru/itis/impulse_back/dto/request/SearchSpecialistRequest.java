package ru.itis.impulse_back.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchSpecialistRequest {

    @JsonProperty("searchString")
    private String specialistName;

    private SearchSpecialistFilterParams filterParams;

    private SearchSpecialistOrder order;
}

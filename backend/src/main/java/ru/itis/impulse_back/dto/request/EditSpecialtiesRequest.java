package ru.itis.impulse_back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditSpecialtiesRequest {
    private String newSpecialty;
}

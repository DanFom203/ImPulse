package ru.itis.impulse_back.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.itis.impulse_back.dto.response.SpecialtiesResponse;
import ru.itis.impulse_back.model.Specialty;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    SpecialtiesResponse toResponse(Specialty specialty);
    List<SpecialtiesResponse> toResponseList(List<Specialty> specialties);
}

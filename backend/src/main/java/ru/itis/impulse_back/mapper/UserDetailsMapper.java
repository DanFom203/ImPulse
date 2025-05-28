package ru.itis.impulse_back.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserDetailsMapper {
    UserDetailsResponse toResponse(User user);
}

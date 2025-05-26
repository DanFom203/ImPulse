package ru.itis.impulse_back.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.model.User;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountModerationResponse toResponse(User user);
    List<AccountModerationResponse> toResponseList(List<User> users);
}

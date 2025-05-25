package ru.itis.impulse_back.dto;


import lombok.*;
import ru.itis.impulse_back.model.User;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUserDto {
    private Long id;
    private String email;
    private String fullName;
    private Date createdAt;

    public static ShortUserDto from(User user) {
        return ShortUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}


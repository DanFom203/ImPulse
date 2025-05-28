package ru.itis.impulse_back.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpecialtyRepositoryImpl implements SpecialtyRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveCustomSpecialty(String name, boolean isCustom) {
        String sql = "INSERT INTO speciality (name, is_custom) VALUES (?, ?) ON CONFLICT (name) DO NOTHING;";
        jdbcTemplate.update(sql, name, isCustom);
    }
}
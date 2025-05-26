package ru.itis.impulse_back.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SpecialistRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public List<User> findSpecialistsByFilter(SearchSpecialistRequest request) {
        String sql = prepareFindSpecialistByFilterSql(request);

        List<User> specialists = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> obj : rows) {
            Optional<User> user = userRepository.findById((Long) obj.get("id"));
            specialists.add(user.get());
        }

        return specialists;

    }

    private String prepareFindSpecialistByFilterSql(SearchSpecialistRequest request) {
        String template = "select distinct a.id as id, a.specialist_appointment_price as specialist_appointment_price, a.specialist_avg_rating as specialist_avg_rating from account a";

        if (request.getFilterParams() != null) {
            boolean hasWhere = false;
            if (request.getFilterParams().getSpecialties() != null
                    && !request.getFilterParams().getSpecialties().isEmpty()) {
                String specialtyListString = request.getFilterParams().getSpecialties().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                template += " join user_speciality us on a.id = user_id where us.speciality_id in ("
                        + specialtyListString
                        + ")";
                hasWhere = true;
            }

            if (!hasWhere) {
                template += " where";
                hasWhere = true;
            } else {
                template += " and";
            }
            

            if (request.getFilterParams().getPrice() != null) {
                Integer minPrice = request.getFilterParams().getPrice().getMinPrice();
                Integer maxPrice = request.getFilterParams().getPrice().getMaxPrice();

                if (minPrice != null && maxPrice != null) {
                    template += " a.specialist_appointment_price between " + minPrice + " and " + maxPrice + " and";
                } else if (minPrice != null) {
                    template += " a.specialist_appointment_price >= " + minPrice + " and";
                } else if (maxPrice != null) {
                    template += " a.specialist_appointment_price <= " + maxPrice + " and";
                }
            }

            if (request.getFilterParams().getMinRating() != null) {
                template += " a.specialist_avg_rating >= " + request.getFilterParams().getMinRating() + " and";
            }
        } else {
            template += " where";
        }

        template += " a.full_name ilike '%" + request.getSpecialistName() + "%'";
        template += " and a.role = 'SPECIALIST'";

        if (request.getOrder() != null) {
            if (request.getOrder().getOrderBy() != null) {
                template += " order by";

                String ordering = request.getOrder().getOrderBy();

                switch (ordering) {
                    case "rating":
                        template += " a.specialist_avg_rating";
                        break;
                    case "price":
                        template += " a.specialist_appointment_price";
                        break;
                }

                if (request.getOrder().getOrderType() != null) {
                    String orderType = request.getOrder().getOrderType();

                    switch (orderType) {
                        case "ascending":
                            template += " asc";
                            break;
                        case "descending":
                            template += " desc";
                            break;
                    }
                }
            }
        }

        System.out.println(template);

        return template;

    }

    public void saveCustomSpecialty(String name, boolean isCustom) {
        String sql = "insert into speciality (name, is_custom) values (?, ?) on conflict (name) do nothing;";
        jdbcTemplate.update(sql, name, isCustom);
    }
}

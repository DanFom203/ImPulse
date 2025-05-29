package ru.itis.impulse_back.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.impulse_back.dto.request.SearchSpecialistRequest;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.exception.SpecialistNotFoundException;
import ru.itis.impulse_back.exception.SpecialtyNotFoundException;
import ru.itis.impulse_back.mapper.UserDetailsMapper;
import ru.itis.impulse_back.model.Specialty;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.SpecialistRepository;
import ru.itis.impulse_back.repository.SpecialtyRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.impl.SpecialistServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialistServiceImplTest {

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private UserDetailsMapper userDetailsMapper;

    @InjectMocks
    private SpecialistServiceImpl specialistService;

    @Test
    void getAllByFilter_ShouldReturnSpecialists() {
        SearchSpecialistRequest request = new SearchSpecialistRequest();
        List<User> expectedSpecialists = List.of(new User(), new User());

        when(specialistRepository.findSpecialistsByFilter(request)).thenReturn(expectedSpecialists);

        List<User> actual = specialistService.getAllByFilter(request);

        assertThat(actual).isEqualTo(expectedSpecialists);
        verify(specialistRepository).findSpecialistsByFilter(request);
    }

    @Test
    void getById_ShouldReturnSpecialist_WhenUserIsSpecialist() {
        Long id = 1L;
        User specialist = new User();
        specialist.setRole(User.UserRole.SPECIALIST);

        when(userRepository.findById(id)).thenReturn(Optional.of(specialist));

        User actual = specialistService.getById(id);

        assertThat(actual).isEqualTo(specialist);
        verify(userRepository).findById(id);
    }

    @Test
    void getById_ShouldThrow_WhenUserNotFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specialistService.getById(id))
                .isInstanceOf(SpecialistNotFoundException.class)
                .hasMessageContaining("Specialist with id " + id + " not found");
    }

    @Test
    void getById_ShouldThrow_WhenUserIsNotSpecialist() {
        Long id = 1L;
        User user = new User();
        user.setRole(User.UserRole.CLIENT);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> specialistService.getById(id))
                .isInstanceOf(SpecialistNotFoundException.class)
                .hasMessageContaining("Specialist with id " + id + " not found");
    }

    @Test
    void updateSpecialties_ShouldUpdateSpecialistSpecialties() {
        Long specialistId = 1L;
        List<Long> specialtyIds = List.of(10L, 20L);

        Specialty s1 = new Specialty();
        Specialty s2 = new Specialty();

        User specialist = new User();
        specialist.setRole(User.UserRole.SPECIALIST);

        when(specialtyRepository.findById(10L)).thenReturn(Optional.of(s1));
        when(specialtyRepository.findById(20L)).thenReturn(Optional.of(s2));
        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        specialistService.updateSpecialties(specialtyIds, specialistId);

        verify(specialtyRepository).findById(10L);
        verify(specialtyRepository).findById(20L);
        verify(userRepository).save(specialist);

        assertThat(specialist.getSpecialties()).containsExactlyInAnyOrder(s1, s2);
    }

    @Test
    void updateSpecialties_ShouldThrow_WhenSpecialtyNotFound() {
        Long specialistId = 1L;
        List<Long> specialtyIds = List.of(10L);

        when(specialtyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specialistService.updateSpecialties(specialtyIds, specialistId))
                .isInstanceOf(SpecialtyNotFoundException.class)
                .hasMessageContaining("Specialty with id 10 not found");
    }

    @Test
    void updateSpecialistInfo_ShouldUpdateBioAndPrice() {
        Long specialistId = 1L;
        String bio = "New bio";
        Integer price = 1500;

        User specialist = new User();
        specialist.setRole(User.UserRole.SPECIALIST);

        UserDetailsResponse response = new UserDetailsResponse();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userDetailsMapper.toResponse(specialist)).thenReturn(response);

        UserDetailsResponse actual = specialistService.updateSpecialistInfo(specialistId, bio, price);

        verify(userRepository).save(specialist);
        verify(userDetailsMapper).toResponse(specialist);

        assertThat(specialist.getSpecialistBio()).isEqualTo(bio);
        assertThat(specialist.getSpecialistAppointmentPrice()).isEqualTo(price);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    void updateSpecialistInfo_ShouldUpdateOnlyBio_WhenPriceNull() {
        Long specialistId = 1L;
        String bio = "Bio only";

        User specialist = new User();
        specialist.setRole(User.UserRole.SPECIALIST);

        UserDetailsResponse response = new UserDetailsResponse();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userDetailsMapper.toResponse(specialist)).thenReturn(response);

        UserDetailsResponse actual = specialistService.updateSpecialistInfo(specialistId, bio, null);

        verify(userRepository).save(specialist);
        verify(userDetailsMapper).toResponse(specialist);

        assertThat(specialist.getSpecialistBio()).isEqualTo(bio);
        assertThat(specialist.getSpecialistAppointmentPrice()).isNull();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    void updateSpecialistInfo_ShouldUpdateOnlyPrice_WhenBioNull() {
        Long specialistId = 1L;
        Integer price = 2000;

        User specialist = new User();
        specialist.setRole(User.UserRole.SPECIALIST);

        UserDetailsResponse response = new UserDetailsResponse();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userDetailsMapper.toResponse(specialist)).thenReturn(response);

        UserDetailsResponse actual = specialistService.updateSpecialistInfo(specialistId, null, price);

        verify(userRepository).save(specialist);
        verify(userDetailsMapper).toResponse(specialist);

        assertThat(specialist.getSpecialistBio()).isNull();
        assertThat(specialist.getSpecialistAppointmentPrice()).isEqualTo(price);
        assertThat(actual).isEqualTo(response);
    }
}

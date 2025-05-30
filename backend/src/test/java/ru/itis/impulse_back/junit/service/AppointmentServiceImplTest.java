package ru.itis.impulse_back.junit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.itis.impulse_back.dto.request.CreateAppointmentRequest;
import ru.itis.impulse_back.exception.AppointmentNotFoundException;
import ru.itis.impulse_back.model.Appointment;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.AppointmentRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.impl.AppointmentServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldSaveAppointment() {
        Long clientId = 1L;
        Long specialistId = 2L;
        Date scheduledDate = new Date();

        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setSpecialistId(specialistId);
        request.setDate(scheduledDate);

        User specialist = User.builder()
                .id(specialistId)
                .specialistAppointmentPrice(100)
                .build();

        User client = User.builder()
                .id(clientId)
                .build();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));


        appointmentService.create(request, clientId);


        verify(appointmentRepository, times(1)).save(argThat(appointment ->
                appointment.getClient().equals(client) &&
                        appointment.getSpecialist().equals(specialist) &&
                        appointment.getScheduledAt().equals(scheduledDate) &&
                        appointment.getPrice().equals(specialist.getSpecialistAppointmentPrice()) &&
                        Boolean.FALSE.equals(appointment.getIsApproved())
        ));
    }

    @Test
    void approve_ShouldSetApproved_WhenAppointmentExistsAndSpecialistMatches() {
        Long appointmentId = 1L;
        Long specialistId = 2L;

        User specialist = User.builder().id(specialistId).build();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .specialist(specialist)
                .isApproved(false)
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        appointmentService.approve(appointmentId, specialistId);

        assertThat(appointment.getIsApproved()).isTrue();
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void approve_ShouldNotApprove_WhenSpecialistDoesNotMatch() {
        Long appointmentId = 1L;
        Long specialistId = 2L;
        Long otherSpecialistId = 3L;

        User specialist = User.builder().id(otherSpecialistId).build();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .specialist(specialist)
                .isApproved(false)
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.approve(appointmentId, specialistId);

        assertThat(appointment.getIsApproved()).isFalse();
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void approve_ShouldThrowException_WhenAppointmentNotFound() {
        Long appointmentId = 1L;
        Long specialistId = 2L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.approve(appointmentId, specialistId))
                .isInstanceOf(AppointmentNotFoundException.class)
                .hasMessageContaining("Appointment with id " + appointmentId + " not found");
    }

    @Test
    void getAllByUserId_ShouldReturnAppointmentsForClient() {
        Long clientId = 1L;

        User client = User.builder()
                .id(clientId)
                .role(User.UserRole.CLIENT)
                .build();

        List<Appointment> appointments = List.of(new Appointment(), new Appointment());

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.findAllByClientId(clientId)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAllByUserId(clientId);

        assertThat(result).isEqualTo(appointments);
    }

    @Test
    void getAllByUserId_ShouldReturnAppointmentsForSpecialist() {
        Long specialistId = 2L;

        User specialist = User.builder()
                .id(specialistId)
                .role(User.UserRole.SPECIALIST)
                .build();

        List<Appointment> appointments = List.of(new Appointment(), new Appointment());

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(appointmentRepository.findAllBySpecialistId(specialistId)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAllByUserId(specialistId);

        assertThat(result).isEqualTo(appointments);
    }
}

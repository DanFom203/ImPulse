package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ru.itis.impulse_back.dto.request.CreateAppointmentRequest;
import ru.itis.impulse_back.exception.AppointmentNotFoundException;
import ru.itis.impulse_back.model.Appointment;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.AppointmentRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.AppointmentService;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public void create(CreateAppointmentRequest request, Long clientId) {
        User specialist = userRepository.findById(request.getSpecialistId()).get();

        Appointment appointment = Appointment.builder()
                .client(userRepository.findById(clientId).get())
                .specialist(specialist)
                .createdAt(new Date())
                .scheduledAt(request.getDate())
                .price(specialist.getSpecialistAppointmentPrice())
                .isApproved(false)
                .build();

        appointmentRepository.save(appointment);
    }

    @Override
    public void approve(Long appointmentId, Long specialistId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found");
        }

        Appointment appointment = appointmentOptional.get();

        if (Objects.equals(appointment.getSpecialist().getId(), specialistId)) {
            appointment.setIsApproved(true);
            appointmentRepository.save(appointment);
        }
    }

    @Override
    public List<Appointment> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId).get();

        switch (user.getRole()) {
            case CLIENT -> {
                return appointmentRepository.findAllByClientId(userId);
            }
            case SPECIALIST -> {
                return appointmentRepository.findAllBySpecialistId(userId);
            }
        }

        return null;
    }
}

package ru.itis.impulse_back.service;


import ru.itis.impulse_back.dto.request.CreateAppointmentRequest;
import ru.itis.impulse_back.model.Appointment;
import java.util.List;

public interface AppointmentService {
    void create(CreateAppointmentRequest request, Long clientId);
    void approve(Long appointmentId, Long specialistId);
    List<Appointment> getAllByUserId(Long userId);
}

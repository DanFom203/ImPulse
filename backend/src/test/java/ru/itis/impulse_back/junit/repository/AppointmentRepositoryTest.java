package ru.itis.impulse_back.junit.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itis.impulse_back.model.Appointment;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.AppointmentRepository;
import ru.itis.impulse_back.repository.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllBySpecialistId() {
        User specialist = userRepository.save(User.builder().email("specialist@test.com").build());
        User client = userRepository.save(User.builder().email("client@test.com").build());

        Appointment app1 = Appointment.builder()
                .specialist(specialist)
                .client(client)
                .scheduledAt(Date.from(Instant.now()))
                .build();

        appointmentRepository.save(app1);

        List<Appointment> appointments = appointmentRepository.findAllBySpecialistId(specialist.getId());

        assertThat(appointments).isNotEmpty();
        assertThat(appointments).allMatch(a -> a.getSpecialist().getId().equals(specialist.getId()));
    }

    @Test
    void testFindAllByClientId() {
        User specialist = userRepository.save(User.builder().email("specialist2@test.com").build());
        User client = userRepository.save(User.builder().email("client2@test.com").build());

        Appointment app1 = Appointment.builder()
                .specialist(specialist)
                .client(client)
                .scheduledAt(Date.from(Instant.now()))
                .build();

        appointmentRepository.save(app1);

        List<Appointment> appointments = appointmentRepository.findAllByClientId(client.getId());

        assertThat(appointments).isNotEmpty();
        assertThat(appointments).allMatch(a -> a.getClient().getId().equals(client.getId()));
    }

    @Test
    void testDeleteAllByClientOrSpecialist() {
        User specialist1 = userRepository.save(User.builder().email("spec1@test.com").build());
        User specialist2 = userRepository.save(User.builder().email("spec2@test.com").build());
        User client1 = userRepository.save(User.builder().email("client1@test.com").build());
        User client2 = userRepository.save(User.builder().email("client2@test.com").build());

        Appointment app1 = Appointment.builder().client(client1).specialist(specialist1).build();
        Appointment app2 = Appointment.builder().client(client1).specialist(specialist2).build();
        Appointment app3 = Appointment.builder().client(client2).specialist(specialist1).build();
        Appointment app4 = Appointment.builder().client(client2).specialist(specialist2).build();

        appointmentRepository.saveAll(List.of(app1, app2, app3, app4));

        appointmentRepository.deleteAllByClientOrSpecialist(client1, specialist1);

        List<Appointment> remaining = appointmentRepository.findAll();

        assertThat(remaining).hasSize(1);
        assertThat(remaining.get(0).getClient()).isEqualTo(client2);
        assertThat(remaining.get(0).getSpecialist()).isEqualTo(specialist2);
    }
}

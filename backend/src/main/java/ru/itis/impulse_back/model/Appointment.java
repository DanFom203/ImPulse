package ru.itis.impulse_back.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private User specialist;

    private Integer price;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "scheduled_at")
    private Date scheduledAt;

    @Column(name = "is_approved")
    private Boolean isApproved;
}

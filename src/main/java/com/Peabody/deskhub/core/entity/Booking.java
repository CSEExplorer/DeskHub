package com.Peabody.deskhub.core.entity;

import com.Peabody.deskhub.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        indexes = {

                @Index(
                        name = "idx_booking_date",
                        columnList = "booking_date"
                ),

                @Index(
                        name = "idx_booking_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_booking_seat",
                        columnList = "seat_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "seat_id",
            nullable = false
    )
    private Seat seat;

    @Column(
            name = "booking_date",
            nullable = false
    )
    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime bookedAt;

    private LocalDateTime cancelledAt;

    @PrePersist
    public void prePersist() {

        if (bookedAt == null) {
            bookedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = BookingStatus.BOOKED;
        }
    }
}
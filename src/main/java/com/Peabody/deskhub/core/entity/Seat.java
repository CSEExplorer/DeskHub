package com.Peabody.deskhub.core.entity;

import com.Peabody.deskhub.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_seat_number",
                        columnNames = "seat_number"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "seat_number",
            nullable = false,
            length = 20
    )
    private String seatNumber;

    /**
     * Values:
     * FIXED
     * FLEXIBLE
     */
    @Column(
            name = "seat_type",
            nullable = false,
            length = 20
    )
    private String seatType;

    /**
     * Only populated for FIXED seats.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}

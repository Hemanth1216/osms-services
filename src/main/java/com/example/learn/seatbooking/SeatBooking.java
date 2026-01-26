package com.example.learn.seatbooking;

import com.example.learn.seat.Seat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seatbookings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @JoinColumn(name = "seat_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Seat seat;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

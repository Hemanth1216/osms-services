package com.example.learn.seat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "seat_no", nullable = false)
    private String seatNumber;
    @Column(nullable = false)
    public Integer floor;
    private String location;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

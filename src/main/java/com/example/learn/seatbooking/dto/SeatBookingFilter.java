package com.example.learn.seatbooking.dto;

import com.example.learn.seatbooking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatBookingFilter {

    private Long id;
    private String seatno;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
}

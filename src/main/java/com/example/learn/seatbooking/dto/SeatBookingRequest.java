package com.example.learn.seatbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatBookingRequest {

    @NotNull(message = "userid is required")
    private Integer userId;
    @NotNull(message = "please select a seat")
    private Integer seatId;
    @NotNull(message = "start time is required")
    private LocalDateTime startTime;
    @NotNull(message = "end time is required")
    private LocalDateTime endTime;
}

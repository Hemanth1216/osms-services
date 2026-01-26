package com.example.learn.seat.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatRequest {

    @NotBlank(message = "Seat number is required")
    @Size(min = 2, max = 20, message = "Seat number length should be b/w 2 to 20")
    private String seatNumber;
    @NotNull(message = "Floor is required")
    @Min(message = "Floor must be at least 1", value = 1)
    @Max(message = "Floor must be at most 7", value=7)
    private Integer floor;
    @Size(max = 100, message = "Location must be at most 100 characters ")
    private String location;
}

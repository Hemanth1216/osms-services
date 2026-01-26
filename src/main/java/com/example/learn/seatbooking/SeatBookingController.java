package com.example.learn.seatbooking;

import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.seatbooking.dto.SeatBookingFilter;
import com.example.learn.seatbooking.dto.SeatBookingRequest;
import com.example.learn.seatbooking.dto.SeatBookingResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SeatBookingController {

    private final SeatBookingService seatBookingService;
    public SeatBookingController(SeatBookingService service) {
        seatBookingService = service;
    }

    @GetMapping("/seatbookings")
    public ResponseEntity<Page<SeatBooking>> getAllSeatBookings(Authentication authentication,
                                                                PageRequestDto pageRequestDto,
                                                                SeatBookingFilter seatBookingFilter) {
        return ResponseEntity.status(HttpStatus.OK).body(seatBookingService.findAllSeatBookings(pageRequestDto, seatBookingFilter));
    }

    @PostMapping("/seatbooking")
    public ResponseEntity<SeatBookingResponse> bookSeat(@Valid @RequestBody SeatBookingRequest seatBookingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatBookingService.bookSeat(seatBookingRequest));
    }

    @DeleteMapping("/seatbooking/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        seatBookingService.cancelBookingRequest(bookingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

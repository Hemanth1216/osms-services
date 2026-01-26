package com.example.learn.seat;

import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.seat.dto.SeatFilter;
import com.example.learn.seat.dto.SeatRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SeatController {

    private final SeatService seatService;
    public SeatController(SeatService service) {
        seatService = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/seats")
    public ResponseEntity<Page<Seat>> getAllSeats(PageRequestDto pageRequestDto, SeatFilter seatFilter) {
        return ResponseEntity.ok(seatService.findAllSeats(pageRequestDto, seatFilter));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/seat/{seatId}")
    public ResponseEntity<Seat> getSeat(@PathVariable Integer seatId) {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.findSeat(seatId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/seat")
    public ResponseEntity<String> addSeat(@Valid @RequestBody SeatRequest seat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.addSeat(seat));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/seat/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Integer seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/seat/{seatId}")
    public ResponseEntity<String> updateSeat(@PathVariable Integer seatId, @Valid @RequestBody Seat seat) {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.updateSeat(seat, seatId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/availability/seats")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestParam Integer floor,
                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return ResponseEntity.status(HttpStatus.OK).
                body(seatService.findAvailableSeats(floor, startTime, endTime));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/availability/seats/now")
    public  ResponseEntity<List<Seat>> getAvailableSeatsNow() {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.findAvailableSeatsNow());
    }

}

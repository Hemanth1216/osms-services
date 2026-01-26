package com.example.learn.seat;

import com.example.learn.common.NotFoundException;
import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.seat.dto.SeatFilter;
import com.example.learn.seat.dto.SeatRequest;
import com.example.learn.seat.specification.SeatSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    public SeatService(SeatRepository repository) {
        seatRepository = repository;
    }

    public Page<Seat> findAllSeats(PageRequestDto pageRequestDto, SeatFilter seatFilter) {
        // Exception handling
        if(pageRequestDto.getPage() < 0) {
            throw new IllegalArgumentException("Page number should not be negative");
        }
        if(pageRequestDto.getSize() <= 0 || pageRequestDto.getSize() > 100) {
            throw new IllegalArgumentException("Page size should be in b/w 1 to 100");
        }

        // Specification
        Specification<Seat> specification = Specification
                .where(SeatSpecification.containsSeatNumber(seatFilter.getSeatno()))
                .and(SeatSpecification.hasFloor(seatFilter.getFloor()))
                .and(SeatSpecification.containsLocation(seatFilter.getLocation()));

        // Pagination
        Sort sort = pageRequestDto.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(pageRequestDto.getSortBy()).descending() : Sort.by(pageRequestDto.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);
        Page<Seat> seats = seatRepository.findAll(specification, pageable);

        if(seats.isEmpty()) {
            throw new NotFoundException("No seats found");
        }
        return seats;
    }

    public Seat findSeat(Integer seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException("Seat not found"));
    }

    public String addSeat(SeatRequest newSeat) {
        Seat seat = new Seat();
        seat.setSeatNumber(newSeat.getSeatNumber());
        seat.setFloor(newSeat.getFloor());
        seat.setLocation(newSeat.getLocation());
        seatRepository.save(seat);
        return "Seat created successfully";
    }

    public void deleteSeat(Integer seatId) {
        Seat seat = findSeat(seatId);
        seatRepository.delete(seat);
    }

    public String updateSeat(Seat seat, Integer seatId) {
        Seat existingSeat = findSeat(seatId);
        existingSeat.setSeatNumber(seat.getSeatNumber());
        existingSeat.setFloor(seat.getFloor());
        existingSeat.setLocation(seat.getLocation());
        seatRepository.save(existingSeat);
        return "Seat updated successfully";
    }

    public List<Seat> findAvailableSeats(Integer floor, LocalDateTime startTime, LocalDateTime endTime) {
        List<Seat> availableSeats = seatRepository.findAvailableSeats(floor, startTime, endTime);
        if(availableSeats.isEmpty()) {
            throw new NotFoundException("No seats available");
        }
        return availableSeats;
    }

    public List<Seat> findAvailableSeatsNow() {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsNow();
        if(availableSeats.isEmpty()) {
            throw new NotFoundException("No seats available right now");
        }
        return availableSeats;
    }
}

package com.example.learn.seatbooking;

import com.example.learn.auth.AuthService;
import com.example.learn.auth.dto.CustomUserDetails;
import com.example.learn.common.NotFoundException;
import com.example.learn.common.dto.PageRequestDto;
import com.example.learn.seat.Seat;
import com.example.learn.seat.SeatService;
import com.example.learn.seatbooking.dto.SeatBookingFilter;
import com.example.learn.seatbooking.dto.SeatBookingRequest;
import com.example.learn.seatbooking.dto.SeatBookingResponse;
import com.example.learn.seatbooking.exceptions.DateTimeValidationException;
import com.example.learn.seatbooking.exceptions.SeatAlreadyBookedException;
import com.example.learn.seatbooking.specification.SeatBookingSpecification;
import com.example.learn.user.UserType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatBookingService {

    private final SeatBookingRepository seatBookingRepository;
    private final SeatService seatService;
    private final AuthService authService;
    public SeatBookingService(SeatBookingRepository repository, SeatService seatService, AuthService authService) {
        this.seatBookingRepository = repository;
        this.seatService = seatService;
        this.authService = authService;
    }

    public SeatBooking findSeatBooking(Long bookingId) {
        SeatBooking seatBooking = seatBookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("No booking found"));
        return seatBooking;
    }

    public Page<SeatBooking> findAllSeatBookings(PageRequestDto pageRequestDto, SeatBookingFilter seatBookingFilter) {
        // Exception handling
        if(pageRequestDto.getPage() < 0) {
            throw new IllegalArgumentException("Page number should not be negative");
        }
        if(pageRequestDto.getSize() <= 0 || pageRequestDto.getSize() > 100) {
            throw new IllegalArgumentException("Page size should be in b/w 1 to 100");
        }

        CustomUserDetails userDetails = authService.getLoggedInUserDetails();
        boolean isAdmin = userDetails.getUserType() == UserType.ADMIN;
        // specification
        Specification<SeatBooking> specification = Specification
                .where(SeatBookingSpecification.hasBookingId(seatBookingFilter.getId()))
                .and(SeatBookingSpecification.containsSeatNumber(seatBookingFilter.getSeatno()))
                .and(SeatBookingSpecification.hasStatus(seatBookingFilter.getStatus()))
                .and(SeatBookingSpecification.startAfter(seatBookingFilter.getStartTime()))
                .and(SeatBookingSpecification.endBefore(seatBookingFilter.getEndTime()));
        if (!isAdmin) {
            specification = specification.and(
                    SeatBookingSpecification.belongsToUser(userDetails.getId())
            );
        }
        // pagination
        Sort sort = pageRequestDto.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(pageRequestDto.getSortBy()).descending() : Sort.by(pageRequestDto.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);

        Page<SeatBooking> seatBookings =
                seatBookingRepository.findAll(specification, pageable);
        return seatBookings;
    }

    @Transactional
    public SeatBookingResponse bookSeat(SeatBookingRequest seatBookingRequest) {
        if(seatBookingRequest.getEndTime().isBefore(seatBookingRequest.getStartTime())) {
            throw new DateTimeValidationException("End time should be greater than start time");
        }
        if(isSeatAlreadyBooked(seatBookingRequest)) {
            throw new SeatAlreadyBookedException("Sorry...seat is already booked for this time. Please try again.");
        }
        Seat seat = seatService.findSeat(seatBookingRequest.getSeatId());
        SeatBooking seatBooking = new SeatBooking();
        seatBooking.setUserId(seatBookingRequest.getUserId());
        seatBooking.setStartTime(seatBookingRequest.getStartTime());
        seatBooking.setEndTime(seatBookingRequest.getEndTime());
        seatBooking.setBookingStatus(BookingStatus.ACTIVE);
        seatBooking.setSeat(seat);
        SeatBooking confirmedSeatBooking = seatBookingRepository.save(seatBooking);
        SeatBookingResponse seatBookingResponse = new SeatBookingResponse(
                confirmedSeatBooking.getId(),
                confirmedSeatBooking.getSeat().getSeatNumber(),
                confirmedSeatBooking.getSeat().getFloor(),
                confirmedSeatBooking.getSeat().getLocation(),
                confirmedSeatBooking.getStartTime(),
                confirmedSeatBooking.getEndTime()
        );
        return seatBookingResponse;
    }

    public Boolean isSeatAlreadyBooked(SeatBookingRequest seatBookingRequest) {
        return seatBookingRepository.findOverLappingBooking(
                seatBookingRequest.getSeatId(),
                seatBookingRequest.getStartTime(),
                seatBookingRequest.getEndTime()
        );
    }

    public void cancelBookingRequest(Long bookingId) {
        SeatBooking seatBooking = findSeatBooking(bookingId);
        seatBooking.setBookingStatus(BookingStatus.CANCELLED);
        seatBookingRepository.save(seatBooking);
    }

}

package com.example.learn.seatbooking;

import com.example.learn.seatbooking.dto.SeatBookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long>, JpaSpecificationExecutor<SeatBooking> {

    @Query("""
               select count(sb) > 0
               from SeatBooking sb
               join sb.seat s
               where s.id = :seatId
               and sb.bookingStatus = 'ACTIVE'
               and sb.startTime < :endTime
               and sb.endTime > :startTime
           """)
    public boolean findOverLappingBooking(@Param("seatId") Integer seatId,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    @Modifying
    @Query("""
                update SeatBooking sb
                set sb.bookingStatus = :completedStatus
                where sb.bookingStatus = :currentStatus
                and sb.endTime < :now
            """)
    public int markStatusCompleted(@Param("currentStatus") BookingStatus currentStatus,
                                    @Param("completedStatus") BookingStatus completedStatus,
                                    @Param("now") LocalDateTime now);

    Page<SeatBooking> findByUserId(Integer userId, Pageable pageable);

    Page<SeatBooking> findAll(Specification<SeatBooking> spec, Pageable pageable);

}


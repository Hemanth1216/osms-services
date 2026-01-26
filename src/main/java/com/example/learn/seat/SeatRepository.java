package com.example.learn.seat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer>, JpaSpecificationExecutor<Seat> {

    @Query("""
        select s
        from Seat s
        where not exists (
            select 1
            from SeatBooking sb
            where sb.seat = s
            and sb.bookingStatus = 'ACTIVE'
            and sb.startTime < :endTime
            and sb.endTime > :startTime
        )
        and s.floor = :floor
        """)
    List<Seat> findAvailableSeats(@Param("floor") Integer floor,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    @Query("""
        select s
        from Seat s
        where not exists (
            select 1
            from SeatBooking sb
            where sb.seat = s
              and sb.bookingStatus = 'ACTIVE'
              and sb.startTime <= CURRENT_TIMESTAMP
              and sb.endTime >= CURRENT_TIMESTAMP
        )
        """)
    List<Seat> findAvailableSeatsNow();

    Page<Seat> findAll(Specification<Seat> spec, Pageable pageable);

}

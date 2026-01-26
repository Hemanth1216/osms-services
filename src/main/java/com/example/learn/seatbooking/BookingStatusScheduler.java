package com.example.learn.seatbooking;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingStatusScheduler {

    private final SeatBookingRepository bookingRepository;

    @Transactional
    @Scheduled(cron = "0 */30 * * * *")
    public void markCompletedBookings() {
        LocalDateTime now = LocalDateTime.now();

        int updatedCount = bookingRepository.markStatusCompleted(
                BookingStatus.ACTIVE,
                BookingStatus.COMPLETED,
                now
        );
        System.out.println("Ran just now and found these: " + updatedCount);

        if (updatedCount > 0) {
            log.info("Marked {} bookings as COMPLETED", updatedCount);
        }
    }

}

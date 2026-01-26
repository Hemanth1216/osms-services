package com.example.learn.seatbooking.specification;

import com.example.learn.seat.Seat;
import com.example.learn.seatbooking.BookingStatus;
import com.example.learn.seatbooking.SeatBooking;
import com.example.learn.user.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SeatBookingSpecification {

    public SeatBookingSpecification() {}

    public static Specification<SeatBooking> hasBookingId(Long bookingId) {
        return ((root, query, cb) ->
                bookingId == null ? null :
                        cb.equal(root.get("id"), bookingId)
        );
    }

    public static Specification<SeatBooking> hasStatus(BookingStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("bookingStatus"), status);
    }

    public static Specification<SeatBooking> startAfter(LocalDateTime start) {
        return (root, query, cb) ->
                start == null ? null :
                        cb.greaterThanOrEqualTo(root.get("startTime"), start);
    }

    public static Specification<SeatBooking> endBefore(LocalDateTime end) {
        return (root, query, cb) ->
                end == null ? null :
                        cb.lessThanOrEqualTo(root.get("endTime"), end);
    }

    public static Specification<SeatBooking> containsSeatNumber(String seatNumber) {
        return ((root, query, cb) -> {
            if (seatNumber == null)
                return null;

            Join<SeatBooking, Seat> seatJoin = root.join("seat");
            return cb.like(
                    cb.lower(root.get("seat").get("seatNumber")), "%" + seatNumber.toLowerCase() + "%"
            );
        });
    }

//    public static Specification<SeatBooking> containsUsername(String name) {
//        return ((root, query, cb) -> {
//            if(name == null) {
//                return null;
//            }
//
//            Join<SeatBooking, User> userJoin = root.join("user");
//            return cb.like(
//                    cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"
//            );
//        });
//    }

    public static Specification<SeatBooking> belongsToUser(Integer userId) {
        return (root, query, cb) ->
                cb.equal(root.get("userId"), userId);
    }

}

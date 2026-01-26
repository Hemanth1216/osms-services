package com.example.learn.seat.specification;

import com.example.learn.seat.Seat;
import com.example.learn.seat.dto.SeatFilter;
import org.springframework.data.jpa.domain.Specification;

public class SeatSpecification {

    public SeatSpecification() {}

    public static Specification<Seat> containsSeatNumber(String seatNumber) {
        return ((root, query, cb) ->
                seatNumber == null ? null :
                cb.like(
                        cb.lower(root.get("seatNumber")), "%" + seatNumber + "%"
                )
        );
    }

    public static Specification<Seat> hasFloor(int floor) {
        return ((root, query, cb) ->
                floor < 1 ? null : cb.equal(root.get("floor"), floor)
        );
    }

    public static Specification<Seat> containsLocation(String location) {
        return ((root, query, cb) ->
                location == null ? null :
                        cb.like(
                                cb.lower(root.get("location")), "%" + location + "%"
                        )
        );
    }

}

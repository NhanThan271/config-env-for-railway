package com.thantruongnhan.doanketthucmon.service;

import com.thantruongnhan.doanketthucmon.entity.Seat;

import java.util.List;

public interface SeatService {

    List<Seat> getAllSeats();

    Seat getSeatById(Long id);

    List<Seat> getSeatsByRoom(Long roomId);

    Seat createSeat(Seat seat);

    Seat updateSeat(Long id, Seat seat);

    void deleteSeat(Long id);
}

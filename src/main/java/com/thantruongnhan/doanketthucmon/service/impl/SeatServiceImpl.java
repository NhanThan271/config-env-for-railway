package com.thantruongnhan.doanketthucmon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.thantruongnhan.doanketthucmon.entity.Room;
import com.thantruongnhan.doanketthucmon.entity.Seat;
import com.thantruongnhan.doanketthucmon.repository.RoomRepository;
import com.thantruongnhan.doanketthucmon.repository.SeatRepository;
import com.thantruongnhan.doanketthucmon.service.SeatService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Override
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    @Override
    public List<Seat> getSeatsByRoom(Long roomId) {
        return seatRepository.findByRoomId(roomId);
    }

    @Override
    public Seat createSeat(Seat seat) {

        if (seat.getRoom() == null || seat.getRoom().getId() == null) {
            throw new RuntimeException("Room is required");
        }

        Room room = roomRepository.findById(seat.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (seatRepository.existsByRoomIdAndRowSeatAndNumber(
                room.getId(), seat.getRowSeat(), seat.getNumber())) {
            throw new RuntimeException("Seat already exists in this room");
        }

        seat.setRoom(room);
        return seatRepository.save(seat);
    }

    @Override
    public Seat updateSeat(Long id, Seat seat) {

        Seat existing = getSeatById(id);

        existing.setRowSeat(seat.getRowSeat());
        existing.setNumber(seat.getNumber());
        existing.setType(seat.getType());

        return seatRepository.save(existing);
    }

    @Override
    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }
}

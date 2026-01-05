package com.thantruongnhan.doanketthucmon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.thantruongnhan.doanketthucmon.entity.Cinema;
import com.thantruongnhan.doanketthucmon.entity.Room;
import com.thantruongnhan.doanketthucmon.repository.CinemaRepository;
import com.thantruongnhan.doanketthucmon.repository.RoomRepository;
import com.thantruongnhan.doanketthucmon.service.RoomService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public List<Room> getRoomsByCinema(Long cinemaId) {
        return roomRepository.findByCinemaId(cinemaId);
    }

    @Override
    public Room createRoom(Room room) {

        if (room.getCinema() == null || room.getCinema().getId() == null) {
            throw new RuntimeException("Cinema is required");
        }

        // optional: check trùng tên phòng trong cùng cinema
        if (roomRepository.existsByCinemaIdAndName(
                room.getCinema().getId(),
                room.getName())) {
            throw new RuntimeException("Room name already exists in this cinema");
        }

        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room room) {

        Room existing = getRoomById(id);

        existing.setName(room.getName());

        if (room.getCinema() != null) {
            existing.setCinema(room.getCinema());
        }

        return roomRepository.save(existing);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

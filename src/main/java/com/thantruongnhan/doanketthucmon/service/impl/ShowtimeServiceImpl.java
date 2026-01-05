package com.thantruongnhan.doanketthucmon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.thantruongnhan.doanketthucmon.entity.Movie;
import com.thantruongnhan.doanketthucmon.entity.Room;
import com.thantruongnhan.doanketthucmon.entity.Showtime;
import com.thantruongnhan.doanketthucmon.repository.MovieRepository;
import com.thantruongnhan.doanketthucmon.repository.RoomRepository;
import com.thantruongnhan.doanketthucmon.repository.ShowtimeRepository;
import com.thantruongnhan.doanketthucmon.service.ShowtimeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
    }

    @Override
    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    @Override
    public List<Showtime> getShowtimesByRoom(Long roomId) {
        return showtimeRepository.findByRoomId(roomId);
    }

    @Override
    public Showtime createShowtime(Showtime showtime) {

        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new RuntimeException("Movie is required");
        }

        if (showtime.getRoom() == null || showtime.getRoom().getId() == null) {
            throw new RuntimeException("Room is required");
        }

        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Room room = roomRepository.findById(showtime.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        showtime.setMovie(movie);
        showtime.setRoom(room);

        return showtimeRepository.save(showtime);
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime showtime) {

        Showtime existing = getShowtimeById(id);

        existing.setStartTime(showtime.getStartTime());
        existing.setFormat(showtime.getFormat());
        existing.setPrice(showtime.getPrice());

        return showtimeRepository.save(existing);
    }

    @Override
    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }
}

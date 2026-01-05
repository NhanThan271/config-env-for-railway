package com.thantruongnhan.doanketthucmon.service;

import com.thantruongnhan.doanketthucmon.entity.Showtime;

import java.util.List;

public interface ShowtimeService {

    List<Showtime> getAllShowtimes();

    Showtime getShowtimeById(Long id);

    List<Showtime> getShowtimesByMovie(Long movieId);

    List<Showtime> getShowtimesByRoom(Long roomId);

    Showtime createShowtime(Showtime showtime);

    Showtime updateShowtime(Long id, Showtime showtime);

    void deleteShowtime(Long id);
}

package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Seat;
import com.thantruongnhan.doanketthucmon.entity.ShowTime;
import com.thantruongnhan.doanketthucmon.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Tìm ghế theo showtime
    List<Seat> findByShowTime(ShowTime showTime);

    // Tìm ghế theo showtime và status
    List<Seat> findByShowTimeAndStatus(ShowTime showTime, SeatStatus status);

    // Tìm ghế available theo showtime
    @Query("SELECT s FROM Seat s WHERE s.showTime = :showTime AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByShowTime(@Param("showTime") ShowTime showTime);

    // Đếm số ghế theo status
    long countByStatus(SeatStatus status);

    // Đếm số ghế theo showtime và status
    long countByShowTimeAndStatus(ShowTime showTime, SeatStatus status);
}
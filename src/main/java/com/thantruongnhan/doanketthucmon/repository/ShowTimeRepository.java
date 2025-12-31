package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Cinema;
import com.thantruongnhan.doanketthucmon.entity.Product;
import com.thantruongnhan.doanketthucmon.entity.ShowTime;
import com.thantruongnhan.doanketthucmon.entity.enums.ShowFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    // Tìm suất chiếu theo phim
    List<ShowTime> findByMovieOrderByShowDateAscShowTimeAsc(Product movie);

    // Tìm suất chiếu theo rạp
    List<ShowTime> findByCinemaOrderByShowDateAscShowTimeAsc(Cinema cinema);

    // Tìm suất chiếu theo phim và rạp
    List<ShowTime> findByMovieAndCinemaOrderByShowDateAscShowTimeAsc(Product movie, Cinema cinema);

    // Tìm suất chiếu theo ngày
    List<ShowTime> findByShowDateOrderByShowTimeAsc(LocalDate showDate);

    // Tìm suất chiếu theo phim và ngày
    List<ShowTime> findByMovieAndShowDateOrderByShowTimeAsc(Product movie, LocalDate showDate);

    // Tìm suất chiếu theo rạp và ngày
    List<ShowTime> findByCinemaAndShowDateOrderByShowTimeAsc(Cinema cinema, LocalDate showDate);

    // Tìm suất chiếu theo phim, rạp và ngày
    List<ShowTime> findByMovieAndCinemaAndShowDateOrderByShowTimeAsc(Product movie, Cinema cinema, LocalDate showDate);

    // Tìm suất chiếu theo định dạng (2D/3D)
    List<ShowTime> findByFormatOrderByShowDateAscShowTimeAsc(ShowFormat format);

    // Tìm suất chiếu đang active
    List<ShowTime> findByIsActiveTrueOrderByShowDateAscShowTimeAsc();

    // Tìm suất chiếu trong khoảng thời gian
    @Query("SELECT st FROM ShowTime st WHERE st.showDate BETWEEN :startDate AND :endDate ORDER BY st.showDate ASC, st.showTime ASC")
    List<ShowTime> findShowTimesBetweenDates(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Tìm suất chiếu của phim trong khoảng thời gian
    @Query("SELECT st FROM ShowTime st WHERE st.movie = :movie AND st.showDate BETWEEN :startDate AND :endDate ORDER BY st.showDate ASC, st.showTime ASC")
    List<ShowTime> findMovieShowTimesBetweenDates(@Param("movie") Product movie,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Tìm suất chiếu còn ghế trống
    @Query("SELECT st FROM ShowTime st WHERE st.availableSeats > 0 AND st.isActive = true ORDER BY st.showDate ASC, st.showTime ASC")
    List<ShowTime> findAvailableShowTimes();

    // Tìm suất chiếu của phim còn ghế trống
    @Query("SELECT st FROM ShowTime st WHERE st.movie = :movie AND st.availableSeats > 0 AND st.isActive = true ORDER BY st.showDate ASC, st.showTime ASC")
    List<ShowTime> findAvailableShowTimesByMovie(@Param("movie") Product movie);

    // Kiểm tra suất chiếu đã tồn tại chưa
    boolean existsByMovieAndCinemaAndShowDateAndShowTimeAndRoomNumber(
            Product movie, Cinema cinema, LocalDate showDate, LocalTime showTime, String roomNumber);

    // Đếm số suất chiếu của phim
    long countByMovie(Product movie);

    // Đếm số suất chiếu của rạp
    long countByCinema(Cinema cinema);

    // Tìm suất chiếu theo phòng và ngày
    List<ShowTime> findByCinemaAndRoomNumberAndShowDate(Cinema cinema, String roomNumber, LocalDate showDate);
}
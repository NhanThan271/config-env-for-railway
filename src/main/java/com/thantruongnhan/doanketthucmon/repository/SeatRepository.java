package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Seat;
import com.thantruongnhan.doanketthucmon.entity.ShowTime;
import com.thantruongnhan.doanketthucmon.entity.enums.SeatStatus;
import com.thantruongnhan.doanketthucmon.entity.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Tìm ghế theo showtime
    List<Seat> findByShowTime(ShowTime showTime);

    // Tìm ghế theo showtime và status
    List<Seat> findByShowTimeAndStatus(ShowTime showTime, SeatStatus status);

    // Tìm ghế available theo showtime
    @Query("SELECT s FROM Seat s WHERE s.showTime = :showTime AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByShowTime(@Param("showTime") ShowTime showTime);

    // Tìm ghế theo row, number và showtime
    Optional<Seat> findByRowAndNumberAndShowTime(String row, Integer number, ShowTime showTime);

    // Tìm ghế theo type và showtime
    List<Seat> findByShowTimeAndType(ShowTime showTime, SeatType type);

    // Đếm số ghế theo status
    long countByStatus(SeatStatus status);

    // Đếm số ghế theo showtime và status
    long countByShowTimeAndStatus(ShowTime showTime, SeatStatus status);

    // Kiểm tra ghế có tồn tại không (theo row, number và showtime)
    boolean existsByRowAndNumberAndShowTime(String row, Integer number, ShowTime showTime);

    // Tìm các ghế đang được giữ chỗ bởi user
    List<Seat> findByReservedBy(com.thantruongnhan.doanketthucmon.entity.User user);

    // Tìm các ghế đã hết hạn giữ chỗ
    @Query("SELECT s FROM Seat s WHERE s.status = 'RESERVED' AND s.reservedUntil < :now")
    List<Seat> findExpiredReservations(@Param("now") LocalDateTime now);

    // Tìm ghế theo showtime và price range
    @Query("SELECT s FROM Seat s WHERE s.showTime = :showTime AND s.price BETWEEN :minPrice AND :maxPrice")
    List<Seat> findByShowTimeAndPriceRange(
            @Param("showTime") ShowTime showTime,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);
}
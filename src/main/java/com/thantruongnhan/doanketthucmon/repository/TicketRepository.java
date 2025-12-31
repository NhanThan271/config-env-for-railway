package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Order;
import com.thantruongnhan.doanketthucmon.entity.ShowTime;
import com.thantruongnhan.doanketthucmon.entity.Ticket;
import com.thantruongnhan.doanketthucmon.entity.User;
import com.thantruongnhan.doanketthucmon.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Tìm ticket theo mã code
    Optional<Ticket> findByTicketCode(String ticketCode);

    // Tìm ticket theo QR code
    Optional<Ticket> findByQrCode(String qrCode);

    // Tìm tickets theo user
    List<Ticket> findByUserOrderByCreatedAtDesc(User user);

    // Tìm tickets theo order
    List<Ticket> findByOrder(Order order);

    // Tìm tickets theo showtime
    List<Ticket> findByShowTime(ShowTime showTime);

    // Tìm tickets theo user và status
    List<Ticket> findByUserAndStatusOrderByCreatedAtDesc(User user, TicketStatus status);

    // Tìm tickets theo status
    List<Ticket> findByStatus(TicketStatus status);

    // Tìm tickets của user trong khoảng thời gian
    @Query("SELECT t FROM Ticket t WHERE t.user = :user " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Ticket> findUserTicketsBetweenDates(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số vé theo status
    long countByStatus(TicketStatus status);

    // Đếm số vé của user
    long countByUser(User user);

    // Đếm số vé theo showtime
    long countByShowTime(ShowTime showTime);

    // Kiểm tra ticket code có tồn tại không
    boolean existsByTicketCode(String ticketCode);

    // Kiểm tra QR code có tồn tại không
    boolean existsByQrCode(String qrCode);

    // Tìm tickets sắp hết hạn (trong vòng X giờ)
    @Query("SELECT t FROM Ticket t WHERE t.showTime.startTime BETWEEN :now AND :deadline " +
            "AND t.status = 'BOOKED'")
    List<Ticket> findTicketsExpiringBefore(
            @Param("now") LocalDateTime now,
            @Param("deadline") LocalDateTime deadline);
}